import org.testng.annotations.*;
import org.mvel2.MVEL;
import org.mvel2.MVELInterpretedRuntime;
import org.mvel2.integration.impl.CachedMapVariableResolverFactory;
import org.mvel2.integration.impl.DefaultLocalVariableResolverFactory;
import org.mvel2.integration.VariableResolverFactory;

import java.util.HashMap;

import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.expression.Expression;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.Tuple;

/**
 * <p><small>
 * Initial author: <a href="mailto:dimitar.dimitrov@gmail.com">ddimitrov</a>,
 * Created: May 13, 2009 10:28:14 AM
 * <small></p>
 */
@Test
public class PrefuseTest {
    public static class Bean {
        public int a, b, c;

        public Bean(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    @Test
    void testContextObjectLookups() {
        VariableResolverFactory variables = new DefaultLocalVariableResolverFactory();
        Bean ctx = new Bean(4, 2, 6);
        String expressionSpec = "(a + b) / c";
//        Expression x = new ColumnExpression();
//
//        MVELInterpretedRuntime runtime = new MVELInterpretedRuntime(expression, ctx, variables);
//        Object result = runtime.parse();
//        assert result.equals(1.0) : result;
    }
}