package br.sahydi.crudspring.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] reportGenerate (String report_name, Map<String,Object> param , ServletContext servletContext ) throws SQLException, JRException {

        //Conexão com Banco de Dados
        Connection connection = jdbcTemplate.getDataSource().getConnection();

        //Carregar o caminho do arquivo Jasper
        String report_path = servletContext.getRealPath("reports")
        + File.separator + report_name + ".jasper";

        //Gerar Relatório
        JasperPrint relatorio = JasperFillManager.fillReport(report_path, param, connection);

        //Salva
        byte [] report_save = JasperExportManager.exportReportToPdf(relatorio);

        //Fecha conexão
        connection.close();

        return report_save;

    }

}
