package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.EnderecoResponse;
import com.serv.oeste.application.dtos.reponses.ViaCepResponse;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.address.AddressNotValidException;
import com.serv.oeste.domain.exceptions.external.ExternalNetworkException;
import com.serv.oeste.domain.exceptions.external.ExternalServerDownException;
import com.serv.oeste.domain.exceptions.external.RestTemplateException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final RestTemplate restTemplate;
    
    public EnderecoResponse getFields(String cep) {
        return getViaCepObject(cep);
    }
    
    protected EnderecoResponse getViaCepObject(String cep) {
        try {
            ViaCepResponse viaCep = restTemplate.getForObject("https://viacep.com.br/ws/{cep}/json", ViaCepResponse.class, cep);
            if (viaCep == null || StringUtils.isBlank(viaCep.logradouro()))
                return new EnderecoResponse(null, null, null);
            return new EnderecoResponse(viaCep);
        }
        catch (HttpClientErrorException e) {
            throw new AddressNotValidException();
        }
        catch (HttpServerErrorException e) {
            throw new ExternalServerDownException(ErrorFields.CEP, "Servidor da ViaCep caiu.");
        }
        catch (ResourceAccessException e) {
            throw new ExternalNetworkException(ErrorFields.CEP, "Problema de rede ao acessar o serviço ViaCep");
        }
        catch (RestClientException e) {
            throw new RestTemplateException(ErrorFields.CEP, "Erro no RestTemplate, consulte um desenvolvedor!");
        }
    }
}