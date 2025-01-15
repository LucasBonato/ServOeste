package com.serv.oeste.service;

import com.serv.oeste.exception.endereco.EnderecoNotValidException;
import com.serv.oeste.exception.viacep.RestTemplateException;
import com.serv.oeste.exception.viacep.ViaCepNetworkException;
import com.serv.oeste.exception.viacep.ViaCepServerDownException;
import com.serv.oeste.models.enums.Codigo;
import com.serv.oeste.models.viacep.ViaCep;
import com.serv.oeste.models.viacep.ViaCepDTO;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final RestTemplate restTemplate;

    public ResponseEntity<ViaCepDTO> getFields(String cep) {
        return ResponseEntity.ok(getViaCepObject(cep));
    }

    protected ViaCepDTO getViaCepObject(String cep){
        try {
            ViaCep viaCep = restTemplate.getForObject("https://viacep.com.br/ws/{cep}/json", ViaCep.class, cep);
            if(StringUtils.isBlank(viaCep.getLogradouro())) return new ViaCepDTO(null);
            return new ViaCepDTO(viaCep.getLogradouro() + "|" + viaCep.getBairro() + "|" + viaCep.getLocalidade());
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
