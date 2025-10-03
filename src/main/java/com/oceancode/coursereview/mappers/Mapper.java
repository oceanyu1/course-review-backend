package com.oceancode.coursereview.mappers;

public interface Mapper<A, B> {
    B mapTo(A a);

    A mapFrom(B b);
}
