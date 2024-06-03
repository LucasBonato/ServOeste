package com.serv.oeste.service;

import com.serv.oeste.exception.endereco.EnderecoNotValidException;
import com.serv.oeste.exception.viacep.RestTemplateException;
import com.serv.oeste.exception.viacep.ViaCepNetworkException;
import com.serv.oeste.exception.viacep.ViaCepServerDownException;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.viacep.ViaCep;
import com.serv.oeste.models.viacep.ViaCepDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
public class EnderecoService {
    @Autowired private RestTemplate restTemplate;

    public ResponseEntity<ViaCepDTO> getFields(String cep) {
        return ResponseEntity.ok(getViaCepObject(cep));
    }

    protected ViaCepDTO getViaCepObject(String cep){
        try {
            ViaCep viaCep = restTemplate.getForObject("https://viacep.com.br/ws/{cep}/json", ViaCep.class, cep);
            if(viaCep == null) return null;
            return new ViaCepDTO(viaCep.getCep().replace("-", ""), viaCep.getLogradouro(), viaCep.getLocalidade(), viaCep.getBairro(), viaCep.getUf());
        } catch (HttpClientErrorException e) {
            throw new EnderecoNotValidException(Codigo.ENDERECO, "CEP inexistente!");
        } catch (HttpServerErrorException e) {
            throw new ViaCepServerDownException();
        } catch (ResourceAccessException e) {
            throw new ViaCepNetworkException("Problema de rede ao acessar o serviço ViaCep");
        } catch (RestClientException e) {
            throw new RestTemplateException("Erro no RestTemplate, consulte um desenvolvedor!");
        }
    }
}
