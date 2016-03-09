package com.example.daniele;

import com.example.daniele.db.DB;

import java.util.List;

import rx.functions.Func1;

public interface Projector<T> extends Func1<List<DB.Event>, T> {

}
