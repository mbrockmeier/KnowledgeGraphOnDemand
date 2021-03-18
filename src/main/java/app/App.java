package app;

import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.tinylog.Logger;
import parser.DBpediaOntology;
import server.RDFOntology;
import server.RDFResource;
import server.SettingResource;
import server.SparqlPage;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Malte Brockmeier
 */
public class App {
    public static void main(String[] args) throws Exception {
        Undertow.Builder undertowBuilder = Undertow.builder().addHttpListener(8080, "0.0.0.0");
        UndertowJaxrsServer server = new UndertowJaxrsServer().start(undertowBuilder);

        DeploymentInfo deploymentInfo = server.undertowDeployment(AppResourceConfig.class)
                .setContextPath("/kgod")
                .setDeploymentName("KGOD")
                .setDefaultEncoding("UTF-8");

        server.deploy(deploymentInfo);

        Logger.info("Server listening on 0.0.0.0:8080. Press ENTER to quit...");

        DBpediaOntology.getInstance().printOntology();

        System.in.read();

        server.stop();
    }

    public static class AppResourceConfig extends Application {
        Set<Class<?>> classes = new HashSet<>();

        public AppResourceConfig() {
            classes.add(RDFResource.class);
            classes.add(RDFOntology.class);
            classes.add(SettingResource.class);
            classes.add(SparqlPage.class);
        }

        public Set<Class<?>> getClasses() {
            return classes;
        }
    }
}
