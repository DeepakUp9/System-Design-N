package testLLCode.Decoratorpattern.webrequestprocessing;

import testLLCode.Decoratorpattern.webrequestprocessing.Authentication.JWTAuthentication;
import testLLCode.Decoratorpattern.webrequestprocessing.Logging.DebugLogging;
import testLLCode.Decoratorpattern.webrequestprocessing.RateLimiting.WindowBasedRateLimiting;
import testLLCode.Decoratorpattern.webrequestprocessing.WebRequest.PostRequest;
import testLLCode.Decoratorpattern.webrequestprocessing.WebRequest.WebRequest;

public class Solution {
    public static void main(String[] args) {
        WebRequest postwebRequest = new PostRequest();
        System.out.println(postwebRequest.makePayment());

        WebRequest jWebRequest = new JWTAuthentication(postwebRequest);
        System.out.println(jWebRequest.makePayment());

        WebRequest windownRateLimiRequest = new WindowBasedRateLimiting(jWebRequest);
        System.out.println(windownRateLimiRequest.makePayment());

        WebRequest debugRequest = new DebugLogging(windownRateLimiRequest);
        System.out.println(debugRequest.makePayment());

    }
}
