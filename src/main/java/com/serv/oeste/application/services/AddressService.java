package com.serv.oeste.application.services;

import com.serv.oeste.application.exceptions.address.AddressNotValidException;
import com.serv.oeste.application.exceptions.RestTemplateException;
import com.serv.oeste.application.exceptions.viacep.ViaCepNetworkException;
import com.serv.oeste.application.exceptions.viacep.ViaCepServerDownException;
import com.serv.oeste.application.dtos.reponses.EnderecoResponse;
import com.serv.oeste.domain.enums.Codigo;
import com.serv.oeste.domain.entities.viacep.ViaCep;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final RestTemplate restTemplate;
    
    public ResponseEntity<EnderecoResponse> getFields(String cep) {
        return ResponseEntity.ok(getViaCepObject(cep));
    }
    
    protected EnderecoResponse getViaCepObject(String cep) {
        try {
            ViaCep viaCep = restTemplate.getForObject("https://viacep.com.br/ws/{cep}/json", ViaCep.class, cep);
            if (viaCep == null || StringUtils.isBlank(viaCep.getLogradouro()))
                return new EnderecoResponse(null, null, null);
            return new EnderecoResponse(viaCep);
        }
        catch (HttpClientErrorException e) {
            throw new AddressNotValidException(Codigo.ENDERECO, "CEP inexistente!");
        }
        catch (HttpServerErrorException e) {
            throw new ViaCepServerDownException();
        }
        catch (ResourceAccessException e) {
            throw new ViaCepNetworkException("Problema de rede ao acessar o serviço ViaCep");
        }
        catch (RestClientException e) {
            throw new RestTemplateException("Erro no RestTemplate, consulte um desenvolvedor!");
        }
    }
}