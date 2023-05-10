package sample;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataStreamJob {

	public static void main(String[] args) throws Exception {
		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		KafkaSource<Request> source = KafkaSource.<Request>builder()
				.setBootstrapServers("localhost:9092")
				.setTopics("flink_input")
				.setStartingOffsets(OffsetsInitializer.latest())
				.setValueOnlyDeserializer(new RequestDeserializationSchema())
				.build();

		DataStream<Request> inputStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

		KafkaSink<RequestWithCount> sink = KafkaSink.<RequestWithCount>builder()
				.setBootstrapServers("localhost:9092")
				.setRecordSerializer(KafkaRecordSerializationSchema.builder()
						.setTopic("flink_output")
						.setValueSerializationSchema(new RequestSerializationSchema())
						.build())
				.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();

		inputStream.
				keyBy(Request::getUserID).
				process(new AnomalyDetector()).
				filter(new RequestFilter()).
				sinkTo(sink);

		env.execute("Flink Java API Skeleton");
	}
}
