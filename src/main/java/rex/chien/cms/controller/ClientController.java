package rex.chien.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rex.chien.cms.domain.Client;
import rex.chien.cms.domain.ClientRequest;
import rex.chien.cms.repository.ClientRepository;
import rex.chien.cms.security.JwtUser;
import rex.chien.cms.security.RoleName;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER, RoleName.OPERATOR})
    public Iterable<Client> getAll() {
        return clientRepository.findAll();
    }

    @RequestMapping(value = "{clientId}", method = RequestMethod.GET)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER, RoleName.OPERATOR})
    public ResponseEntity<?> get(@PathVariable long clientId) {
        Optional<Client> targetClient = clientRepository.findById(clientId);

        return ResponseEntity.of(targetClient);
    }

    @RequestMapping(value = "bulk", method = RequestMethod.PUT)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.OPERATOR})
    public Client create(@RequestBody ClientRequest clientRequest, Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Client client = new Client();
        client.setName(clientRequest.getName());
        client.setPhone(clientRequest.getPhone());
        client.setEmail(clientRequest.getEmail());
        client.setCreatedBy(jwtUser.getId());
        client.setCreatedAt(new Date());

        clientRepository.save(client);

        return client;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.OPERATOR})
    public List<Client> bulkCreate(@RequestBody List<ClientRequest> clientRequests, Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        List<Client> clients = clientRequests.stream()
                .map(clientRequest -> {
                    Client client = new Client();
                    client.setName(clientRequest.getName());
                    client.setPhone(clientRequest.getPhone());
                    client.setEmail(clientRequest.getEmail());
                    client.setCreatedBy(jwtUser.getId());
                    client.setCreatedAt(new Date());
                    return client;
                }).collect(Collectors.toList());

        clientRepository.saveAll(clients);

        return clients;
    }

    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER})
    public ResponseEntity<?> update(@RequestBody ClientRequest clientRequest, Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Optional<Client> targetClient = clientRepository.findById(clientRequest.getId());

        if (targetClient.isPresent()) {
            Client client = targetClient.get();
            client.setName(clientRequest.getName());
            client.setPhone(clientRequest.getPhone());
            client.setEmail(clientRequest.getEmail());
            client.setUpdatedBy(jwtUser.getId());
            client.setUpdatedAt(new Date());

            clientRepository.save(client);

            return ResponseEntity.ok(client);
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "{clientId}", method = RequestMethod.DELETE)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER})
    public ResponseEntity<?> delete(@PathVariable long clientId) {
        clientRepository.deleteById(clientId);

        return ResponseEntity.ok().build();
    }
}
