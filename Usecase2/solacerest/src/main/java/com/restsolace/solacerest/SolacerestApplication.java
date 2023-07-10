package com.restsolace.solacerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.restsolace.solacerest.Service.CallSmtpApi;
import com.solace.messaging.MessagingService;
import com.solace.messaging.config.SolaceProperties.AuthenticationProperties;
import com.solace.messaging.config.SolaceProperties.ServiceProperties;
import com.solace.messaging.config.SolaceProperties.TransportLayerProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SpringBootApplication
public class SolacerestApplication {
	
	static CallSmtpApi callSmtpApi;
    private static final String SAMPLE_NAME = SolacerestApplication.class.getSimpleName();
    private static final String QUEUE_NAME = "Annual-Practice-Cert-Queue";
    private static final String API = "Java";
    
    private static volatile int msgRecvCounter = 0;                 // num messages received
    private static volatile boolean hasDetectedRedelivery = false;  // detected any messages being redelivered?
    private static volatile boolean isShutdown = false;             // are we done?

    // remember to add log4j2.xml to your classpath
    private static final Logger logger = LogManager.getLogger();  // log4j2, but could also use SLF4J, JCL, etc.
    public static void main(String... args) throws InterruptedException, IOException {
		SpringApplication.run(SolacerestApplication.class, args);

        System.out.println(API + " " + SAMPLE_NAME + " initializing...");

        final Properties properties = new Properties();
        properties.setProperty(TransportLayerProperties.HOST, "localhost:55554");          // host:port
        properties.setProperty(ServiceProperties.VPN_NAME,  "default");     // message-vpn
        properties.setProperty(AuthenticationProperties.SCHEME_BASIC_USER_NAME, "admin");      // client-username
        properties.setProperty(AuthenticationProperties.SCHEME_BASIC_PASSWORD, "admin");  // client-password

        
        properties.setProperty(TransportLayerProperties.RECONNECTION_ATTEMPTS, "20");  // recommended settings
        properties.setProperty(TransportLayerProperties.CONNECTION_RETRIES_PER_HOST, "5");

        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build();
        messagingService.connect();  // blocking connect
        messagingService.addServiceInterruptionListener(serviceEvent -> {
            logger.warn("### SERVICE INTERRUPTION: "+serviceEvent.getCause());
            //isShutdown = true;
        });
        messagingService.addReconnectionAttemptListener(serviceEvent -> {
            logger.info("### RECONNECTING ATTEMPT: "+serviceEvent);
        });
        messagingService.addReconnectionListener(serviceEvent -> {
            logger.info("### RECONNECTED: "+serviceEvent);
        });

        // this receiver assumes the queue already exists and has a topic subscription mapped to it
        // if not, first create queue with PubSub+Manager, or SEMP management API
        final PersistentMessageReceiver receiver = messagingService
                .createPersistentMessageReceiverBuilder()
                .build(Queue.durableExclusiveQueue(QUEUE_NAME));
        try {
        	receiver.start();
        } catch (RuntimeException e) {
            logger.error(e);
            System.err.printf("%n*** Could not establish a connection to queue '%s': %s%n", QUEUE_NAME, e.getMessage());
            System.err.println("  or see the SEMP CURL scripts inside the 'semp-rest-api' directory.");
            System.err.println("NOTE: see HowToEnableAutoCreationOfMissingResourcesOnBroker.java sample for how to construct queue with consumer app.");
            System.err.println("Exiting.");
            return;
        	
        }

        // asynchronous anonymous receiver message callback
        receiver.receiveAsync(message -> {
        	msgRecvCounter++;
        	if (message.isRedelivered()) {  // useful check
                // this is the broker telling the consumer that this message has been sent and not ACKed before.
                // this can happen if an exception is thrown, or the broker restarts, or the network disconnects
                // perhaps an error in processing? Should do extra checks to avoid duplicate processing
                hasDetectedRedelivery = true;
        	}
            // Messages are removed from the broker queue when the ACK is received.
            // Therefore, DO NOT ACK until all processing/storing of this message is complete.
            // NOTE that messages can be acknowledged from any thread.
        	System.out.println(message + "HEHE");
			callSmtpApi.callSmtp(message.toString());
            receiver.ack(message);  // ACKs are asynchronous
        });

        // async queue receive working now, so time to wait until done...
        System.out.println(SAMPLE_NAME + " connected, and running. Press [ENTER] to quit.");
        while (System.in.available() == 0 && !isShutdown) {
            Thread.sleep(4000);  // wait 4 second
            System.out.printf("%s %s Received msgs/s: %,d%n",API,SAMPLE_NAME,msgRecvCounter);  // simple way of calculating message rates
            msgRecvCounter = 0;
            if (hasDetectedRedelivery) {
                System.out.println("*** Redelivery detected ***");
                hasDetectedRedelivery = false;  // only show the error once per second
            }
        }
        isShutdown = true;
        receiver.terminate(1500L);
        Thread.sleep(1000);
        messagingService.disconnect();
        System.out.println("Main thread quitting.");
    }

}



