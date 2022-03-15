package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest // spring boot가 제공하는 test. 기존 테스트와 달리 실제 스프링이 구동되고 db 연결까지 된다.
public class MessageSourceTest {

    @Autowired
    MessageSource ms;
    // 스프링 부트가 자동으로 messageSource를 스프링 빈으로 등록해준다.
    // 우리는 messageSource를 의존관계로 주입해서 사용한다.
    // 스프링 부트가 자동으로 messageSource를 스프링 빈으로 등록할 때
    // application.properties 에 있는 설정 정보를 읽으면서 스프링 빈으로 등록한다.
    // application.properties 에 있는 설정 정보를 읽은 스프링 부트는
    // messages.properties 와 messages_en.properties 파일을 같이 읽는다.


    /**
     * 가장 단순한 테스트는 메시지 코드로 hello 를 입력하고 나머지 값은 null 을 입력했다.
     * locale 정보가 없으면 basename 에서 설정한 기본 이름 메시지 파일을 조회한다.
     * basename 으로 messages 를 지정 했으므로 messages.properties 파일에서 데이터 조회한다.
     */
    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        // locale 를 null로 설정하면 default properties가 실행된다.
        assertThat(result).isEqualTo("안녕");
    }

    /**
     * 메시지가 없는 경우에는 NoSuchMessageException 이 발생한다.
     */
    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    /**
     * 메시지가 없어도 기본 메시지( defaultMessage )를 사용하면 기본 메시지가 반환된다.
     */
    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메세지", null);
        assertThat(result).isEqualTo("기본 메세지");
    }

    /**
     * 다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다.
     * hello.name=안녕 {0} ➡ Spring 단어를 매개변수로 전달 ➡ 안녕 Spring
     */
    @Test
    void argumentMessage() {
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(result).isEqualTo("안녕 Spring");
    }

    /**
     * ms.getMessage("hello", null, null) : locale 정보가 없으므로 messages 를 사용
     * ms.getMessage("hello", null, Locale.KOREA) : locale 정보가 있지만,
     * message_ko 가 없으므로 messages 를 사용
     */
    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    /**
     * ms.getMessage("hello", null, Locale.ENGLISH)
     * locale 정보가 Locale.ENGLISH 이므로 messages_en 을 찾아서 사용
     */
    @Test
    void enLang() {
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }


}
