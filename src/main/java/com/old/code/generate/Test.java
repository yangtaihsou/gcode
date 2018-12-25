package com.old.code.generate;

import java.math.BigDecimal;

/**
 * User: yangkuan@old.com
 * Date: 16-6-7
 * Time: 上午10:39
 */
public class Test {

    public static void main(String[] args) {

        BigDecimal loanRate = new BigDecimal(new Double(0.055));//约定贷款利率
        BigDecimal loadDays = new BigDecimal(new Integer(365+20));//约定理财期限加20天
        BigDecimal yearDays = new BigDecimal(new Integer(365));
        BigDecimal principalBalance  = new BigDecimal(new Double(100));
        BigDecimal loanInterest = principalBalance.multiply(loanRate).multiply(loadDays).divide(yearDays,BigDecimal.
                ROUND_HALF_UP);
        loanInterest = loanInterest.setScale(4,   BigDecimal.ROUND_HALF_UP);
        System.out.println(loanInterest);

        BigDecimal test =  new BigDecimal("3.454583");
        BigDecimal test1 =  new BigDecimal("3.454584");
      //  System.out.println(test.setScale(4,   BigDecimal.ROUND_HALF_UP));

        String msgcontent="感谢您购买京东白拿，您已投资了%1s元%2s（理财产品名称），同时为您申请了一笔%3s元的消费信托贷款，理财的部分到期收益将用于偿还该消费信托贷款，请您知悉。【京东金融】";
        msgcontent =  String.format(msgcontent.toString(),test,"sdfsd",test);
        System.out.println(msgcontent);

        System.out.println(test.compareTo(test1));
    }

}
