package app;

import extraction.KnowledgeGraphConfiguration;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.tinylog.Logger;
import parser.DBpediaOntology;
import server.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Malte Brockmeier
 */
public class App {
    public static void main(String[] args) throws Exception {
        int port = KnowledgeGraphConfiguration.getServerPort();
        Undertow.Builder undertowBuilder = Undertow.builder().addHttpListener(port, "0.0.0.0");
        UndertowJaxrsServer server = new UndertowJaxrsServer().start(undertowBuilder);

        DeploymentInfo deploymentInfo = server.undertowDeployment(AppResourceConfig.class)
                .setContextPath("/kgod")
                .setDeploymentName("KGOD")
                .setDefaultEncoding("UTF-8");

        server.deploy(deploymentInfo);

        Logger.info("Server listening on 0.0.0.0:" + port + ". Press ENTER to quit...");

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
            classes.add(SparqlResource.class);
            classes.add(ExtendedSparqlResource.class);
        }

        public Set<Class<?>> getClasses() {
            return classes;
        }
    }
}
