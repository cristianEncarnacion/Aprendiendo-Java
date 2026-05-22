package gm.zona_fit;

import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	public static void main(String[] args) {

		//Levantar la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
