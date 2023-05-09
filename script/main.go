package main

import (
	"context"
	"encoding/json"
	"fmt"
	"strconv"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
)

func main() {
	produce(context.Background())
}

const (
	topic          = "flink_input"
	broker1Address = "localhost:9092"
)

type Request struct {
	UserID string `json:"UserID"`
}

func produce(ctx context.Context) {

	// intialize the writer with the broker addresses, and the topic
	w := kafka.NewWriter(kafka.WriterConfig{
		Brokers: []string{broker1Address},
		Topic:   topic,
	})

	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)

		userID := uuid.New().String()

		go func(wg *sync.WaitGroup, userID string, i int) {
			defer wg.Done()

			for j := 0; j < 5; j++ {
				r := Request{
					UserID: userID,
				}

				b, err := json.Marshal(r)
				if err != nil {
					panic("could not marshall struct" + err.Error())
				}

				err = w.WriteMessages(ctx, kafka.Message{
					Key:   []byte(strconv.Itoa(i)),
					Value: b,
				})
				if err != nil {
					panic("could not write message " + err.Error())
				}

				if i == 8 || i == 9 {
					fmt.Println(userID)
					err = w.WriteMessages(ctx, kafka.Message{
						Key:   []byte(strconv.Itoa(i)),
						Value: b,
					})
					if err != nil {
						panic("could not write message " + err.Error())
					}
				}

				time.Sleep(1 * time.Second)
			}

		}(&wg, userID, i)

	}

	wg.Wait()

}
