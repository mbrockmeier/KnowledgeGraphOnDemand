package app;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.tinylog.Logger;
import server.RDFResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static io.undertow.Handlers.resource;

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

        System.in.read();

        server.stop();

        /*
        UndertowJaxrsServer server = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(AppResourceConfig.class.getName());
        deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");

        DeploymentInfo deploymentInfo = server.undertowDeployment(deployment, "/");
        deploymentInfo.setClassLoader(App.class.getClassLoader());
        deploymentInfo.setDeploymentName("KGOD");
        deploymentInfo.setContextPath("/kgod");
        deploymentInfo.setDefaultEncoding("UTF-8");

        deploymentInfo.addListener(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));

        server.deploy(deploymentInfo);

        server.addResourcePrefixPath("/",
                resource(new ClassPathResourceManager(App.class.getClassLoader()))
                        .addWelcomeFiles("index.html"));

        Undertow.Builder builder = Undertow.builder()
                .addHttpListener(8080, "localhost");

        server.start(builder);
        */
    }

    public static class AppResourceConfig extends Application {
        Set<Class<?>> classes = new HashSet<>();

        public AppResourceConfig() {
            classes.add(RDFResource.class);
        }

        public Set<Class<?>> getClasses() {
            return classes;
        }
    }
}
