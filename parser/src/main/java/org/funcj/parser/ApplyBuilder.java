package org.funcj.parser;

import org.funcj.util.Functions.*;

import static org.funcj.parser.Parser.ap;

/**
 * ApplyBuilder combines parsers via successive calls to {@code and} and {@code andL}.
 * <p>
 * ApplyBuilder provides a fluent interface for combining parsers.
 * The first two parsers are combined by a calling the {@link Parser#map(F)},
 * which returns an {@code ApplyBuilder} instance.
 * Each successive parser is incorporated by passing it to a call to {@code and} or {@code andL}.
 * The chain of calls is concluded by calling {@code map} with a handler for the parse results.
 * <p>
 * ApplyBuilder is a concise and more readable way of using {@code Parser#ap} -
 * {@code pa.and(pb).and(pc).map(f)} is equivalent to {@code ap(ap(ap(pa.map(f), pb), pc), pd)}.
 */
public class ApplyBuilder {
    public static class _2<I, A, B> {
        private final Parser<I, A> pa;
        private final Parser<I, B> pb;

        _2(Parser<I, A> pa, Parser<I, B> pb) {
            this.pa = pa;
            this.pb = pb;
        }

        public <R> Parser<I, R> map(F<A, F<B, R>> f) {
            return ap(pa.map(f), pb);
        }

        public <R> Parser<I, R> map(F2<A, B, R> f) {
            return map(f.curry());
        }

        public <C> _2<I, A, B> andL(Parser<I, C> pc) {
            return new _2<I, A, B>(pa, pb.andL(pc));
        }

        public <C> _3<C> and(Parser<I, C> pc) {
            return new _3<C>(pc);
        }

        public class _3<C> {
            private final Parser<I, C> pc;

            private _3(Parser<I, C> pc) {
                this.pc = pc;
            }

            public <R> Parser<I, R> map(F<A, F<B, F<C, R>>> f) {
                return ap(_2.this.map(f), pc);
            }

            public <R> Parser<I, R> map(F3<A, B, C, R> f) {
                return map(f.curry());
            }

            public <D> _3<C> andL(Parser<I, D> pd) {
                return new _3<C>(pc.andL(pd));
            }

            public <D> _4<D> and(Parser<I, D> pd) {
                return new _4<D>(pd);
            }

            public class _4<D> {
                private final Parser<I, D> pd;

                private _4(Parser<I, D> pd) {
                    this.pd = pd;
                }

                public <R> Parser<I, R> map(F<A, F<B, F<C, F<D, R>>>> f) {
                    return ap(_3.this.map(f), pd);
                }

                public <R> Parser<I, R> map(F4<A, B, C, D, R> f) {
                    return map(f.curry());
                }

                public <E> _4<D> andL(Parser<I, E> pe) {
                    return new _4<D>(pd.andL(pe));
                }

                public <E> _5<E> and(Parser<I, E> pe) {
                    return new _5<E>(pe);
                }

                public class _5<E> {
                    private final Parser<I, E> pe;

                    private _5(Parser<I, E> pe) {
                        this.pe = pe;
                    }

                    public <R> Parser<I, R> map(F<A, F<B, F<C, F<D, F<E, R>>>>> f) {
                        return ap(_4.this.map(f), pe);
                    }

                    public <R> Parser<I, R> map(F5<A, B, C, D, E, R> f) {
                        return map(f.curry());
                    }

                    public <G> _5<E> andL(Parser<I, G> pg) {
                        return new _5<E>(pe.andL(pg));
                    }

                    public <G> _6<G> and(Parser<I, G> pg) {
                        return new _6<G>(pg);
                    }

                    public class _6<G> {
                        private final Parser<I, G> pg;

                        private _6(Parser<I, G> pg) {
                            this.pg = pg;
                        }

                        public <R> Parser<I, R> map(F<A, F<B, F<C, F<D, F<E, F<G, R>>>>>> f) {
                            return ap(_5.this.map(f), pg);
                        }

                        public <R> Parser<I, R> map(F6<A, B, C, D, E, G, R> f) {
                            return map(f.curry());
                        }

                        public <H> _6<G> andL(Parser<I, H> ph) {
                            return new _6<G>(pg.andL(ph));
                        }
                    }
                }
            }
        }
    }
}
