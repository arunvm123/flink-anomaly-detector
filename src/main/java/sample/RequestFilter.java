package sample;

import org.apache.flink.api.common.functions.FilterFunction;

public class RequestFilter implements FilterFunction<RequestWithCount> {

    @Override
    public boolean filter(RequestWithCount request) throws Exception {
        System.out.println(request.Count > 5);
        return request.Count > 5;
    }
}
