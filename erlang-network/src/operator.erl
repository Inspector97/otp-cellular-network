%%%-------------------------------------------------------------------
%%% @author artem
%%% @copyright (C) 2019, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 11. Дек. 2019 21:45
%%%-------------------------------------------------------------------
-module(operator).
-author("artem").
-behaviour(gen_server).

-export([start_link/0, stop/0, sms/3, register/2]).
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
  terminate/2, code_change/3]).


%%-record(state, {}).

start_link() ->
  gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

stop() ->
  gen_server:cast({local, ?MODULE}, stop).

register(Pid, Number) ->
  gen_server:call({local, ?MODULE} , {register, {Pid, Number}}).

sms(From, To, Message) ->
  gen_server:call({local, ?MODULE}, {sms, {From, To, Message}}).

init([]) ->
  {ok, []}.

forward(Pid, From, To, Message) ->
  gen_server:call(Pid, {smsForward, {From, To, Message}}).

handle_call({sms, {From, To, Message}}, _From, State) ->
  io:format("SMS: from: ~p to: ~p data: ~p~n", [From, To, Message]),

%%  looking for 'To' number in register state
  RetList = lists:foldl(
    fun({Pid,NameTo}, TempList) ->
      if (To =:= NameTo) -> [Pid | TempList];
        true -> TempList
      end
    end,
    [], State),

%%  send for specified name and pid
  lists:foreach(
    fun(P) ->
      spawn(fun() -> forward(P, From, To, Message) end)
    end,
    RetList),

  {reply, {oksms, {From, To, Message}}, State};

handle_call({register, {Pid, Number}}, _From, State) ->
  io:format("Client ~p connecting with base station pid:~p~n", [Number, Pid]),
  RetList = lists:foldl(
    fun({P,NameFrom}, TempList) ->
      if (Number =:= NameFrom) -> TempList;
        true -> [{P,NameFrom} | TempList]
      end
    end,
    [], State),
  NewClientList = [{Pid, Number} | RetList],

  {reply, okregister, NewClientList};

handle_call(_Request, _From, State) ->
  {reply, ok, State}.

handle_cast(stop, State) ->
  {stop, normal, State};
handle_cast(_Msg, State) ->
  {noreply, State}.

handle_info(_Msg, State) ->
  {noreply, State}.

terminate(_Reason, _State) ->
  ok.

code_change(_OldVsn, State, _Extra) ->
  {ok, State}.
