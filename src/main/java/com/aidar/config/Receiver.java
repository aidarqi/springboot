package com.aidar.config;

import com.aidar.domain.User;
import com.aidar.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * 监听的业务类，实现接口MessageListener
 * Created by hzlbo on 2016/7/1.
 */

//@RabbitListener(containerFactory = "rabbitListenerContainer", queues = AmqpConfig.QUEUE_NAME)
public class Receiver implements ChannelAwareMessageListener {
    public Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate template;

    @Override
    public void onMessage(Message message, Channel channel) {
        logger.info("##### = {}", new String(message.getBody()));
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(new String(message.getBody()), User.class);
        } catch (IOException e) {
            logger.error("消息转换失败{}",e);
        }
        try {
            //模拟耗时操作
            //TimeUnit.SECONDS.sleep(10);
            this.userRepository.save(user);
            logger.info("消息消费成功{}");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);//手动消息应答
        } catch (Exception e) {
            logger.error("消息消费失败{}",e);
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);//true 重新放入队列
            try {
                /*
                 *deliveryTag:该消息的index
                 *multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
                 *requeue：被拒绝的是否重新入队列,若为true,将会一直被循环加入该队列,直到消费成功
                */
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);//对于处理不了的异常消息
            } catch (IOException e1) {
                logger.error("丢弃转移",e);
                e1.printStackTrace();
            }
            //发送到失败队列
            template.convertAndSend(AmqpConfig.EXCHANGE, AmqpConfig.ROUTINGKEY_FAIL, user);
        }
    }


}
