package cl.rgonzalez.memoria;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RSServiceListener implements VaadinServiceInitListener {

//    @Value("${tupos.app.style}")
//    String appStyle;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        VaadinService vs = event.getSource();

//        vs.addSessionInitListener(initEvent -> {
//            log.info("A new Session has been initialized!");
//            System.out.println("  appStyle: " + appStyle);
////            if (appStyle.equals("online")) {
//                String ip = initEvent.getSession().getBrowser().getAddress();
//                System.out.println("  adress: " + ip);
//
//
//            }
//            initEvent.getSession().setAttribute();
//        });
//        vs.addUIInitListener(initEvent -> log.info("A new UI has been initialized!"));
//        vs.addSessionDestroyListener(initEvent -> log.info("A new Session has been initialized!"))
    }

//    private String tryFindZoneId(String ip){
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL("https://ipapi.co/" + ip + "/timezone");
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//
//            int status = con.getResponseCode();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//
//            System.out.println("  content: " + content);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            con.disconnect();
//        }
//    }
}