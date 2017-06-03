package org.funcj.codec;

import org.funcj.codec.TestDataBase.NoPublicEmptyCtor;
import org.funcj.codec.xml.XmlCodecCore;
import org.junit.Assert;
import org.w3c.dom.Node;

public class XmlCodecTest extends TestBase {
    final static XmlCodecCore codec = new XmlCodecCore();

    static {
        codec.registerCodec(NoPublicEmptyCtor.class, new NoPublicEmptyCtorCodec());
    }

    @Override
    protected <T> void roundTrip(T val, Class<T> clazz) {
        codec.setNewDocument();
        final Node node = codec.encode(clazz, val, "test");
        //java.lang.System.out.println(nodeToString(codec.doc, true));

        final T val2 = codec.decode(clazz, node);

        Assert.assertEquals(val, val2);
    }


    static class NoPublicEmptyCtorCodec implements Codec<NoPublicEmptyCtor, Node> {

        @Override
        public Node encode(NoPublicEmptyCtor val, Node out) {
            return codec.booleanCodec().encode(val.flag, out);
        }

        @Override
        public NoPublicEmptyCtor decode(Node in) {
            return NoPublicEmptyCtor.create(codec.booleanCodec().decode(in));
        }
    }
}
