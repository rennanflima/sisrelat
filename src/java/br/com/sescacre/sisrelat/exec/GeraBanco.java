package br.com.sescacre.sisrelat.exec;

import br.com.sescacre.sisrelat.dao.UsuariosDao;
import br.com.sescacre.sisrelat.entidades.Usuarios;
import br.com.sescacre.sisrelat.util.GeraSenha;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GeraBanco {

    private static UsuariosDao ud = new UsuariosDao();
    private static Usuarios u = new Usuarios();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        SchemaExport se = new SchemaExport(configuration);
        se.create(true, true);
        u.setAutorizacao("ROLE_GER");
        u.setLogin("admsesc");
        u.setSenha(new GeraSenha().ecripta("admin"));
        try {
            ud.salvar(u);
        } catch (Exception ex) {
            System.out.println("O administrador nao foi criado com sucesso!!!");
        }
    }
}
