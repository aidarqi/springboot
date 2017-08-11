import com.aidar.config.AmqpConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @desc
 * @date 17-7-31
 */
public class RoutingSendDirect {
    public static final String EXCHANGE   = "spring-boot-exchange";
    public static final String ROUTINGKEY = "spring-boot-routingKey";
    public static final String ROUTINGKEY_FAIL = "spring-boot-routingKey-failure";
    public static final String QUEUE_NAME = "spring-boot-queue";
    public static final String QUEUE_NAME_FAIL = "spring-boot-queue-failure";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //      声明交换器
//        channel.exchangeDeclare(AmqpConfig.EXCHANGE, "direct");
        //      发送消息
       channel.queueDeclare(QUEUE_NAME, true, false,false,null);

        String message = "{\n" + "  \"uname\": \"user2\",\n"
            + "  \"upwd\": \"000000\",\n" + "  \"usex\": true,\n"
            + "  \"uemail\": \"8jodh7jq@sohu.com\",\n" + "  \"createtime\": 1499822400000,\n"
            + "  \"modtime\": 1499822400000\n" + "}" ;
//        String message = "this is a message" ;

        /*void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body)
            throws IOException;
         *routingKey：路由键，#匹配0个或多个单词，*匹配一个单词，在topic exchange做消息转发用
         *mandatory：true：如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
         *          那么会调用basic.return方法将消息返还给生产者。false：出现上述情形broker会直接将消息扔掉
         *immediate：true：如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者，那么这条消息不会放入队列中。
         *          当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，该消息会通过basic.return方法返还给生产者。
         *BasicProperties ：需要注意的是BasicProperties.deliveryMode，0:不持久化 1：持久化 这里指的是消息的持久化，
         *                  配合channel(durable=true),queue(durable)可以实现，即使服务器宕机，消息仍然保留
         *简单来说：mandatory标志告诉服务器至少将该消息route到一个队列中，否则将消息返还给生产者；
         *        immediate标志告诉服务器如果该消息关联的queue上有消费者，则马上将消息投递给它，如果所有queue都没有消费者，直接把消息返还给生产者，不用将消息入队列等待消费者了。
        */
        channel.basicPublish(AmqpConfig.EXCHANGE, AmqpConfig.ROUTINGKEY, null, message.getBytes());
        System.out.println(" [x] Sent '" +  "':'" + message + "'");
        channel.close();
        connection.close();

    }
}
