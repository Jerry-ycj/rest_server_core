package mizuki.project.core.restserver.modules.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Service
public class SpringSessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public void checkAndUpdateSession(HttpSession httpSession, Model model, String... keys){
        String sessionid =(String) model.asMap().get("sessionId");
        if (sessionid==null) return;
        Session session1 = sessionRepository.findById(sessionid);
        if(session1!=null && !httpSession.getId().equals(sessionid)) {
            for (String key:keys){
                session1.setAttribute(key,httpSession.getAttribute(key));
            }
            sessionRepository.save(session1);
        }
    }

}
