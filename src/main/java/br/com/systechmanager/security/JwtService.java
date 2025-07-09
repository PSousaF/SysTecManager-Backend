package br.com.systechmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.systechmanager.entity.Users;
import br.com.systechmanager.exception.InactiveUserException;
import br.com.systechmanager.exception.PasswordInvalidException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

@Service
public class JwtService {

    //@Value("${security.jwt.expiration}")
    private String expiracao = "90";

    //@Value("${security.jwt.signature-key}")
    private String chaveAssinatura = "c25vQwIUvzN3ozeWhCSmlxhdVF1";

    public String gerarToken( Users usuario ){
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString).minusHours(3);  //o minus verificar depois;
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);
        if(usuario.getAtivo() == 0)
        	throw new InactiveUserException();
        HashMap<String, Object> claimsData = new HashMap<>();
        claimsData.put("exp", data);
        claimsData.put("sub", usuario.getUsername());
        if(usuario.getPermissao() >= 0)
        	claimsData.put("permissao", usuario.getPermissao());
        if(usuario.getAtivo() >= 0)
        	claimsData.put("ativo", usuario.getAtivo());
        return Jwts
                .builder()
                .setSubject(usuario.getUsername())
                .setExpiration(data)
                .setClaims(claimsData)
                .signWith( SignatureAlgorithm.HS512, chaveAssinatura )
                .compact();
    }

    
    private Claims obterClaims( String token ) throws ExpiredJwtException {
        return Jwts
                 .parser()
                 .setSigningKey(chaveAssinatura)  //(DatatypeConverter.parseBase64Binary(chaveAssinatura))
                 .parseClaimsJws(token)
                 .getBody();
    }

    public boolean tokenValido( String token ){ //não ta funcionando
        try{
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            System.out.println("Data " + claims.getExpiration());
            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
            

            LocalDateTime dataHoraExpiracao = LocalDateTime.now().minusHours(3);
            Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
            Date datas = Date.from(instant);
            System.out.println("instant " + instant + "  +  " + datas);
            
            System.out.println("Data " + dataExpiracao);
            System.out.println("Data Claims " + data);
            System.out.println("Verificando token " + dataExpiracao + " - " + data);
            System.out.println("AQUII " + LocalDateTime.now().isAfter(data));
            
            if(LocalDateTime.now().isAfter(data)) {
                System.out.println("Retornou true ou sej OK ");
            }
            else {
                System.out.println("Continua ERRADO ");
            }
            
            
            
            return !LocalDateTime.now().isAfter(data); //o correto é tirar o !
        }catch (Exception e){
            System.out.println("claims " +  e.getMessage());
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }
    

    
    public boolean verifyDashOK(int idCompany, int idUser) {
    	boolean verified = true;
    	//AQUi vai verificar se o usuário é o correto da empresa, ex: se batem o id da empresa no user se a filial pode também
    	
    	return verified;
    }
}

/*
  public static CanAccessDashboard = "CanAccessDashboard";
  public static CanAccessAudit = "CanAccessAudit";
  public static CanAccessPDFAudit = "CanAccessPDFAudit";
  public static CanAccessConsultancy = "CanAccessConsultancy";
  public static CanAccessPDFConsultancy = "CanAccessPDFConsultancy";
  public static CanAccessTraining = "CanAccessTraining";
  public static CanAccessVisits = "CanAccessVisits";
  public static CanAccessAnotherCompany = "CanAccessAnotherCompany";
  public static CanAccessStatistics = "CanAccessStatistics";
 */
