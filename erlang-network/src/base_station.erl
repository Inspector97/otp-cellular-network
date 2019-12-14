%%%-------------------------------------------------------------------
%%% @author artem
%%% @copyright (C) 2019, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 11. Дек. 2019 21:45
%%%-------------------------------------------------------------------
-module(base_station).
-author("artem").
-behaviour(gen_server).

-export([start_link/0, stop/0]).
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
  terminate/2, code_change/3]).

-record(state, {}).

%%TODO Несколько станций
start_link() ->
  gen_server:start_link({global, baseStation}, ?MODULE, [], []).

stop() ->
  gen_server:cast({global, baseStation}, stop).

init([]) ->
  {ok, #state{}}.

handle_call(_Request, _From, State) ->
  {reply, ok, State}.

handle_cast(stop, State) ->
  {stop, normal, State};
handle_cast(_Msg, State) ->
  {noreply, State}.

handle_info({sms, {Number, Message}}, State) ->
  io:format("SMS for Number ~p '~p'~n", [Number, Message]),
  {noreply, State};
handle_info(_Msg, State) ->
  {noreply, State}.

terminate(_Reason, _State) ->
  ok.

code_change(_OldVsn, State, _Extra) ->
  {ok, State}.