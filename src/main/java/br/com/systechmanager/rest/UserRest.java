package br.com.systechmanager.rest;

import lombok.RequiredArgsConstructor;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.systechmanager.config.GenericFunctions;
import br.com.systechmanager.dto.CredentialsDTO;
import br.com.systechmanager.entity.Clients;
import br.com.systechmanager.entity.Users;
import br.com.systechmanager.repository.UserRepository;
import br.com.systechmanager.repository.ClientsRepository;
import br.com.systechmanager.security.JwtService;
import br.com.systechmanager.security.UserAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api("Usuário")
public class UserRest {

	@Autowired
	private UserAuth usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ClientsRepository clientRepository;
	
	@Autowired
	private GenericFunctions statusReturn;

	public UserRest( UserRepository userRepository ) {
		this.userRepository = userRepository;
	}

	@PostMapping("/new")
	@ApiOperation("Adicionar novo usuário")
	@ResponseStatus(HttpStatus.CREATED)
	public Users salvar( @RequestBody @Valid Users user  ){
		System.out.println(user.getPassword());
		String senhaCriptografada = passwordEncoder.encode(user.getPassword());
		System.out.println(user.getPassword());
		System.out.println(senhaCriptografada);
		user.setPassword(senhaCriptografada);
		return usuarioService.salvar(user);
	}

	
	@PostMapping("/auth")
	@ApiOperation("Login usuário App")
	/*@ApiResponses(
			@ApiResponse(code = 200, message = "")
			)*/
	public ResponseEntity<HashMap<String, Object>> autenticarApp(@RequestBody CredentialsDTO  credenciais){
		HashMap<String, Object> data = new HashMap<>();
		try{
			GenericFunctions.log("Tentativa de login, usuário: " + credenciais.getUsername());
			Users usuario = Users.builder()
					.username(credenciais.getUsername())
					.password(credenciais.getPassword()).build();
			UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
			Users u = userRepository.getByUsername(credenciais.getUsername());
			if(u.getAtivo() > 0) {
				String token = jwtService.gerarToken(u);
				data.put("user", usuario.getUsername());
				data.put("cargo", u.getCargo());
				data.put("nome", u.getNome());
				data.put("permissao", u.getPermissao());
				data.put("ativo", u.getAtivo());
				data.put("token", token);
				GenericFunctions.log("Usuário Logado: " + usuario.getUsername());
				return ResponseEntity.ok(statusReturn.getStatusObject(data, true));
			}
			else {
				GenericFunctions.log("Usuário Inativo - id:" + u.getId() +", usuario:"+ usuario.getUsername());
				data.put("error", "Usuário Bloqueado/Inativo, favor contactar ADM");
				return ResponseEntity.badRequest().body(statusReturn.getStatusObject(data, false));
			}
		} catch (Exception e ){
			GenericFunctions.log("Erro: " + e.getMessage());
			data.put("error", e.getMessage());
			return ResponseEntity.badRequest().body(statusReturn.getStatusObject(data, false));
		}
	}
	
	@GetMapping("{id}")
	@ApiOperation("Retornar dados do usuário")
	public ResponseEntity<HashMap<String, Object>> getUserById( @PathVariable Integer id ){
		return ResponseEntity.ok(statusReturn.getStatusObject(userRepository.findById(id), true));
	}

	@PutMapping("{id}")
	@ApiOperation("Atualizar usuário")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update( @PathVariable Integer id, @RequestBody @Valid Users user ){
		HashMap<String, Object> returnItens = new HashMap<>();
		userRepository
		.findById(id)
		.map( userExist -> {
			//user.setId(userExist.getId());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            	userExist.setPassword(user.getPassword());
            }
            else {
            	userExist.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userExist.setNome(user.getNome());
            userExist.setUsername(user.getUsername());
            userExist.setEndereco(user.getEndereco());
            userExist.setTelefone(user.getTelefone());
            userExist.setEmail(user.getEmail());
            userExist.setCargo(user.getCargo());
            userExist.setPermissao(user.getPermissao());
            userExist.setAtivo(user.getAtivo());
            userRepository.save(userExist);
			returnItens.put("success", true);
			returnItens.put("data", userExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}

	@PutMapping("disable/{id}")
	@ApiOperation("Desativar usuário")
	public void inative( @PathVariable Integer id){
		HashMap<String, Object> returnItens = new HashMap<>();

		System.out.println("vaichamar");
		userRepository
		.findById(id)
		.map( userExist -> {
			System.out.println("Achou");
			System.out.println(userExist.getUsername());
            userExist.setAtivo(0);
            userRepository.save(userExist);
			returnItens.put("success", true);
			returnItens.put("data", userExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}	 
	

	@GetMapping("active/{id}")
	@ApiOperation("Ativar usuário")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void active( @PathVariable Integer id){
		HashMap<String, Object> returnItens = new HashMap<>();
		userRepository
		.findById(id)
		.map( userExist -> {
            userExist.setAtivo(1);
            userRepository.save(userExist);
			returnItens.put("success", true);
			returnItens.put("data", userExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}

	@GetMapping("disable/{id}")
	@ApiOperation("Desativar usuário")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void disable( @PathVariable Integer id){
		HashMap<String, Object> returnItens = new HashMap<>();
		userRepository
		.findById(id)
		.map( userExist -> {
            userExist.setAtivo(0);
            userRepository.save(userExist);
			returnItens.put("success", true);
			returnItens.put("data", userExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}	
	
	@GetMapping("/all_users")
	@ApiOperation("Retornar dados do usuário")
	public ResponseEntity<?> listAllOnwers(Model model) {
        try {
		List<Users> listOnwers = userRepository.findAll();
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("success", true);
		returnItens.put("data", listOnwers);
    	return ResponseEntity.ok(returnItens);
	    } catch (DataException ex) {
	        return ResponseEntity.status(401).body("Sem Dados e/ou Não autorizado");
	    } catch (Exception ex) {
	        return ResponseEntity.status(500).body("Ocorreu um erro no servidor, erro: " + ex.getMessage());
	    }
	}

}
