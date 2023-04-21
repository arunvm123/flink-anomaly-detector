package sample;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class AnomalyDetector extends KeyedProcessFunction<String, Request, RequestWithCount> {

    private transient ValueState<Integer> countState;
    private transient ValueState<Long> timerState;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<Integer> countDescriptor = new ValueStateDescriptor<>(
                "count",
                Types.INT);
        countState = getRuntimeContext().getState(countDescriptor);

        ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<>(
                "timer-state",
                Types.LONG);
        timerState = getRuntimeContext().getState(timerDescriptor);
    }

    @Override
    public void processElement(Request request, Context context, Collector<RequestWithCount> collector) throws Exception {
        Integer currentCount = countState.value();
        if (currentCount == null) {
            long timer = context.timerService().currentProcessingTime() + (15 * 1000);
            context.timerService().registerProcessingTimeTimer(timer);
            timerState.update(timer);
            currentCount = 0;
        }
        RequestWithCount r = new RequestWithCount();

        countState.update(currentCount+1);
        r.Count = currentCount+1;
        r.UserID = request.UserID;

        System.out.println(request.getUserID());
        System.out.println(currentCount);
        System.out.println(timerState.value());

        collector.collect(r);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<RequestWithCount> out) {
        System.out.println("TIMER AND COUNTER cleared");
        timerState.clear();
        countState.clear();
    }
}
