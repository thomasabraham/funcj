package org.typemeta.funcj.parser;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assume;
import org.junit.runner.RunWith;
import org.typemeta.funcj.data.Chr;
import org.typemeta.funcj.functions.Functions.F;
import org.typemeta.funcj.tuples.Tuple2;

import static org.hamcrest.CoreMatchers.not;
import static org.typemeta.funcj.parser.Parser.ap;

@RunWith(JUnitQuickcheck.class)
public class ParserPropTests {

    @Property
    public void pureConsumesNoInput(char c1) {
        final Input<Chr> input = Input.of("");

        final Parser<Chr, Chr> parser = Parser.pure(Chr.valueOf(c1));

        TestUtils.ParserCheck.parser(parser)
                .withInput(input)
                .succeedsWithResult(Chr.valueOf(c1), input);
    }

    @Property
    public void mapTransformsValue(char c1) {
        final Input<Chr> input = Input.of(String.valueOf(c1));

        final Parser<Chr, Chr> parser =
                Combinators.value(Chr.valueOf(c1))
                        .map(c -> Chr.valueOf(c.charValue() + 1));

        TestUtils.ParserCheck.parser(parser)
                .withInput(input)
                .succeedsWithResult(Chr.valueOf(c1 + 1), input.next());
    }

    @Property
    public void apAppliesF(char c1) {
        final Input<Chr> input = Input.of(String.valueOf(c1));

        final F<Chr, Chr> f = (Chr c) -> Chr.valueOf(c.charValue() + 1);

        final Parser<Chr, Chr> parser = ap(f, Combinators.any());

        TestUtils.ParserCheck.parser(parser)
                .withInput(input)
                .succeedsWithResult(Chr.valueOf(c1 + 1), input.next());
    }

    @Property
    public void apChainsParsers(char c1, char c2) {
        Assume.assumeThat(c1, not(c2));

        final Chr cc1 = Chr.valueOf(c1);
        final Chr cc2 = Chr.valueOf(c2);

        // String.toCharArray returns a new array each time, so ensure we call it only once.
        final char[] ca12 = String.valueOf("" + c1 + c2).toCharArray();
        final char[] ca11 = String.valueOf("" + c1 + c1).toCharArray();

        final Parser<Chr, Tuple2<Chr, Chr>> parser = ap(ap(a -> b -> Tuple2.of(a, b), Combinators.value(cc1)), Combinators.value(cc2));

        TestUtils.ParserCheck.parser(parser)
                .withInput(Input.of(ca12))
                .succeedsWithResult(Tuple2.of(cc1, cc2), Input.of(ca12).next().next());

        TestUtils.ParserCheck.parser(parser)
                .withInput(Input.of(ca11))
                .fails();
    }

    @Property
    public void orAppliesEitherParser(char c1, char c2, char c3) {
        Assume.assumeThat(c1, not(c2));
        Assume.assumeThat(c1, not(c3));
        Assume.assumeThat(c2, not(c3));

        final Input<Chr> input1 = Input.of(String.valueOf(c1));
        final Input<Chr> input2 = Input.of(String.valueOf(c2));
        final Input<Chr> input3 = Input.of(String.valueOf(c3));

        final Chr cc1 = Chr.valueOf(c1);
        final Chr cc2 = Chr.valueOf(c2);

        final Parser<Chr, Chr> parser = Combinators.value(cc1).or(Combinators.value(cc2));

        TestUtils.ParserCheck.parser(parser)
                .withInput(input1)
                .succeedsWithResult(cc1, input1.next());

        TestUtils.ParserCheck.parser(parser)
                .withInput(input2)
                .succeedsWithResult(cc2, input2.next());

        TestUtils.ParserCheck.parser(parser)
                .withInput(input3)
                .fails();
    }

    @Property
    public void andWithMapAppliesF(char c1, char c2) {
        // String.toCharArray returns a new array each time, so ensure we call it only once.
        final char[] data = ("" + c1 + c2).toCharArray();
        final Input<Chr> input = Input.of(data);

        final Parser<Chr, Tuple2<Chr, Chr>> parser =
                Combinators.<Chr>any()
                        .and(Combinators.any())
                        .map(Tuple2::of);

        final Input<Chr> expInp = Input.of(data).next().next();

        TestUtils.ParserCheck.parser(parser)
                .withInput(input)
                .succeedsWithResult(Tuple2.of(Chr.valueOf(c1), Chr.valueOf(c2)), expInp);
    }
}
