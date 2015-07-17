package bbejeck.lambdas;

import java.util.function.UnaryOperator;

/**
 * User: Bill Bejeck
 * Date: 8/20/14
 * Time: 10:57 PM
 */
public class LambdaExpressExamples {

    public static void main(String[] args) {
        UnaryOperator<Integer> expression_addFive =  x ->  x + 5;

        UnaryOperator<String> expression_block = s -> { StringBuilder sb = new StringBuilder();
                                                        sb.append("Start[")
                                                          .append(s)
                                                          .append("]End");
                                                        return sb.toString(); };
    }
}
