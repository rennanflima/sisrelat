package br.com.sescacre.sisrelat.exec;

//import br.com.uninorte.siscodis.dao.UsersDAO;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GeraBanco {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        SchemaExport se = new SchemaExport(configuration);
        se.create(true, true);
    }
}
