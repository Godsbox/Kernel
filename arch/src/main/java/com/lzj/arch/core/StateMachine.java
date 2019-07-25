/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import android.support.v4.util.ArrayMap;

import com.lzj.arch.core.Contract.PassiveView;

import timber.log.Timber;

/**
 * @author 吴吉林
 */
public class StateMachine {

    private ArrayMap<Class<?>, State<? extends PassiveView, ? extends Model>> states = new ArrayMap<>();

    private State<? extends PassiveView, ? extends Model> cachedState;

    private static final State<PassiveView, Model> NULL_STATE = new State<PassiveView, Model>() {
        @Override
        public void render(PassiveView view, Model model) {
            Timber.d("NULL_STATE#render() called");
            // 空实现
        }
    };

    <V extends PassiveView, M extends Model> void setCachedState(State<V, M> state) {
        this.cachedState = state;
    }

    <V extends PassiveView, M extends Model> State<V, M> getCachedState() {
        return (State<V, M>) cachedState;
    }

    <V extends PassiveView, M extends Model> void addState(State<V, M> state) {
        if (state == null) {
            return;
        }
        states.put(state.getClass(), state);
    }

    <V extends PassiveView, M extends Model> State<V, M> getState(Class<?> clazz) {
        State<V, M> state = (State<V, M>) states.get(clazz);
        return state == null ? (State<V, M>) NULL_STATE : state;
    }
}
