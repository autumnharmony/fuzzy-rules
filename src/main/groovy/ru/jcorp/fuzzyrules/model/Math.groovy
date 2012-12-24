package ru.jcorp.fuzzyrules.model

import ru.jcorp.fuzzyrules.production.Algebra

/**
 * @author artamonov
 */
class Math {
    static ThreadLocal<Algebra> algebraThreadLocal = new ThreadLocal<>()

    static Algebra getAlgebra() {
        return algebraThreadLocal.get()
    }

    static void setAlgebra(Algebra algebra) {
        this.algebraThreadLocal.set(algebra)
    }
}