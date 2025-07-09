package br.com.systechmanager.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.systechmanager.config.GenericFunctions;
import br.com.systechmanager.entity.Bid;
import br.com.systechmanager.entity.Budget;
import br.com.systechmanager.entity.Clients;
import br.com.systechmanager.entity.Moviments;
import br.com.systechmanager.entity.Supplier;
import br.com.systechmanager.entity.Users;
import br.com.systechmanager.repository.BidRepository;
import br.com.systechmanager.repository.BudgetReposiroty;
import br.com.systechmanager.repository.ClientsRepository;
import br.com.systechmanager.repository.MovimentsRepository;
import br.com.systechmanager.repository.SupplierRepository;
import br.com.systechmanager.repository.UserRepository;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/client")
public class ClientsAndSuppliers {
	
    //@Value("${save.file.img}")
    private String dirSave = "C:/WebService/img/";

    @Autowired
    MovimentsRepository movimentsRepository;
	
    @Autowired
	BidRepository bidRepository;
	
	@Autowired
	ClientsRepository clientsRepository;
	
	@Autowired
	BudgetReposiroty budgetReposiroty;
	
	@Autowired
	SupplierRepository suppliersRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GenericFunctions generic;
	
	@GetMapping("/all_clients")
	public HashMap<String, Object> listAll(Model model) {
		List<Clients> listUsers = clientsRepository.findAll();
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("success", true);
		returnItens.put("data", listUsers);
		return returnItens;
	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public Clients salvar( @RequestBody @Valid Clients client  ){
		return clientsRepository.save(client);
	}
	
	@GetMapping("/all_budget")
	public HashMap<String, Object> listAllBudget(Model model) {
		List<Budget> listBuget = budgetReposiroty.getBuget();
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("success", true);
		returnItens.put("data", listBuget);
		return returnItens;
	}

	
	@GetMapping("/seach/{item}")
    public ResponseEntity<?> seachCliorOnwer(@PathVariable String item) throws Exception {
        try {
    		HashMap<String, Object> returnItens = new HashMap<>();
        	List<Clients>  materials = clientsRepository.getLocaCli(item);
    		returnItens.put("success", true);
    		returnItens.put("data", materials);
        	return ResponseEntity.ok(returnItens);
        } catch (DataException ex) {
            return ResponseEntity.status(401).body("Sem Dados e/ou Não autorizado");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Ocorreu um erro no servidor, erro: " + ex.getMessage());
        }
    }	
	
	@GetMapping("/seach_budget/{item}")
    public ResponseEntity<?> seachBudget(@PathVariable String item) throws Exception {
        try {
    		HashMap<String, Object> returnItens = new HashMap<>();
        	List<Budget>  materials = budgetReposiroty.getLocalizaBuget(item);
    		returnItens.put("success", true);
    		returnItens.put("data", materials);
        	return ResponseEntity.ok(returnItens);
        } catch (DataException ex) {
            return ResponseEntity.status(401).body("Sem Dados e/ou Não autorizado");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Ocorreu um erro no servidor, erro: " + ex.getMessage());
        }
    }

	@GetMapping("/all_supliers")
	public HashMap<String, Object> listAllSupliers(Model model) {
		List<Supplier> listUsers = suppliersRepository.findAll();
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("success", true);
		returnItens.put("data", listUsers);
		return returnItens;
	}
	
	@GetMapping("/list_orders")
	public HashMap<String, Object> listAllOrders(Model model) {
		List<Budget> listUsers = budgetReposiroty.getOrder();
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("success", true);
		returnItens.put("data", listUsers);
		return returnItens;
	}
	

	@GetMapping("disable/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inative( @PathVariable Integer id){
		HashMap<String, Object> returnItens = new HashMap<>();
		suppliersRepository
		.findById(id)
		.map( supplierExist -> {
			supplierExist.setAtivo(0);
			suppliersRepository.save(supplierExist);
			returnItens.put("success", true);
			returnItens.put("data", supplierExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}	 
	
	@GetMapping("active/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void active( @PathVariable Integer id){
		HashMap<String, Object> returnItens = new HashMap<>();
		suppliersRepository
		.findById(id)
		.map( supplierExist -> {
			supplierExist.setAtivo(1);
			suppliersRepository.save(supplierExist);
			returnItens.put("success", true);
			returnItens.put("data", supplierExist);
			return returnItens;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Usuário não encontrado") );
	}
	

	@PostMapping("/new_budget")
	public ResponseEntity<?> SaveBudget(@RequestBody HashMap<String, Object> dataReceipt){
		
		HashMap<String, Object> data = new HashMap<>();

		try {
			HashMap<String, Object> budgetInfo = (HashMap<String, Object>) dataReceipt.get("data");
			HashMap<String, String> dataInfoImg = (HashMap<String, String>) dataReceipt.get("img");
			String bid = generic.generateUuid();
			String sit = "Em análise";
			String str = "Pendente";
			String app =  "Não";
	        if(budgetInfo.containsKey("aparroved")) {
	        	System.out.println(app + " - " + budgetInfo.containsKey("aparroved"));
	        	app = Boolean.valueOf(budgetInfo.get("aparroved").toString()) == true ? "Sim": "Não";
	        	System.out.println(app + " - " + budgetInfo.containsKey("aparroved"));
	        }
			List<Bid> dataBidList = new ArrayList<>(); 
	        if(dataInfoImg.size() > 0) {
		        for (Entry<String, String> entry : dataInfoImg.entrySet()) {
		            String key = entry.getKey(); 
		            Object value = entry.getValue();
		            Bid dataBid = new Bid();
		            
		            dataBid.setBid(bid);
		            dataBid.setItem(key);
		            dataBid.setData(String.valueOf(value));
		            dataBidList.add(dataBid);
		        }
	        }
	        Budget budget = new Budget();
	        if(budgetInfo.containsKey("id")) {
	        	budget.setId(Integer.valueOf(budgetInfo.get("id").toString()));
	        }
	        if(budgetInfo.containsKey("bid")) {
	        	bid = budgetInfo.get("bid").toString();
	        }
	        if(budgetInfo.containsKey("situation")) {
	        	sit = budgetInfo.get("situation").toString();
	        }
	        if(budgetInfo.containsKey("detalhe")) {
		        budget.setDetail(budgetInfo.get("detalhe").toString());
	        }
	        if(budgetInfo.containsKey("valor")) {
		        budget.setValueItem(budgetInfo.get("valor").toString());
	        }
	        if(budgetInfo.containsKey("obs")) {
		        budget.setObservation(budgetInfo.get("obs").toString());
	        }
	        if(budgetInfo.containsKey("marca")) {
		        budget.setDeviceBrand(budgetInfo.get("marca").toString());
	        }
	        if(budgetInfo.containsKey("serie")) {
		        budget.setSerie(budgetInfo.get("serie").toString());
	        }
	        if(budgetInfo.containsKey("defeito")) {
		        budget.setDefect(budgetInfo.get("defeito").toString());
	        }
	        if(budgetInfo.containsKey("analise")) {
		        budget.setReview(budgetInfo.get("analise").toString());
	        }
	        if(budgetInfo.containsKey("causa")) {
		        budget.setPossibleCauses(budgetInfo.get("causa").toString());
	        }
	        if(budgetInfo.containsKey("qtde")) {
		        budget.setQuantity(Integer.valueOf(budgetInfo.get("qtde").toString()));
	        }
	        if(budgetInfo.containsKey("tipo")) {
		        budget.setDeviceType(budgetInfo.get("tipo").toString());
	        }
	        if(budgetInfo.containsKey("modelo")) {
		        budget.setModel(budgetInfo.get("modelo").toString());
	        }
	        if(budgetInfo.containsKey("pcutility")) {
		        budget.setPieces(budgetInfo.get("pcutility").toString());
	        }
	        budget.setBid(bid);
	        budget.setIdCli(Integer.valueOf(budgetInfo.get("id_cliente").toString()));
	        budget.setIdResp(Integer.valueOf(budgetInfo.get("id_answerable").toString()));
	        budget.setSituation(sit);
	        budget.setStating(str);
	        budget.setAparroved(app);

	        budgetReposiroty.save(budget);
	        if(dataInfoImg.size() > 0) {
	        	bidRepository.saveAll(dataBidList);
	        }

			data.put("bid", bid);	
			data.put("success", true);	
			return ResponseEntity.ok(data);
		} catch (Exception ex) {
			System.out.println("erro " + ex.getMessage());
			data.put("motivo", "Erro ao salvar orçamento, motivo: " + ex.getMessage());
			data.put("success", false);	
			return ResponseEntity.status(500).body(data);
		}
		
	}
	
	@GetMapping("get_image/{bid}")
	public ResponseEntity<?> getImageBid( @PathVariable String bid){
		HashMap<String, Object> returnItens = new HashMap<>();
		List<Bid> bidItens = bidRepository.findByBid(bid);
		Budget budget = budgetReposiroty.findByBid(bid);
		Optional<Clients> client = clientsRepository.findById(budget.getIdCli());
		Optional<Users> user = userRepository.findById(budget.getIdCli());
		returnItens.put("data", bidItens);	
		returnItens.put("client", client);	
		returnItens.put("user", user);	
		returnItens.put("success", true);	
		return ResponseEntity.ok(returnItens);
	}
	
	@GetMapping("get_values")
	public ResponseEntity<?> getValues(){
		HashMap<String, Object> returnItens = new HashMap<>();
		List<Moviments>  values_in = movimentsRepository.getValuesIn();
		List<Moviments>  valudes_out = movimentsRepository.getValuesOut();
		List<Budget> pieces = budgetReposiroty.getPieces();
		returnItens.put("values_in", values_in);	
		returnItens.put("values_out", valudes_out);	
		returnItens.put("pieces", pieces);	
		returnItens.put("success", true);	
		return ResponseEntity.ok(returnItens);
	}
	
	
	@PostMapping("update_values")
	public ResponseEntity<?> getUpdateValues(@RequestBody Moviments data){
		HashMap<String, Object> returnItens = new HashMap<>();
		returnItens.put("data", movimentsRepository.save(data));	
		returnItens.put("success", true);	
		return ResponseEntity.ok(returnItens);
	}	
}
