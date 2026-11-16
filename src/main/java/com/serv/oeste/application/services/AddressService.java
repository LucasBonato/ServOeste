package com.serv.oeste.application.services;

import com.serv.oeste.application.contracts.clients.ViaCepClient;
import com.serv.oeste.application.dtos.reponses.EnderecoResponse;
import com.serv.oeste.application.dtos.reponses.ViaCepResponse;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.address.AddressNotValidException;
import com.serv.oeste.domain.exceptions.external.ExternalNetworkException;
import com.serv.oeste.domain.exceptions.external.ExternalServerDownException;
import com.serv.oeste.domain.exceptions.external.RestTemplateException;
import com.serv.oeste.domain.utils.StringUtils;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.observation.annotation.ObservationKeyValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final ViaCepClient viaCepClient;

    @Observed(name = "address.lookup")
    public EnderecoResponse getFields(@ObservationKeyValue(key = "cep", value = "{cep}") String cep) {
        log.debug("address.lookup.started cep={}", cep);
        try {
            ViaCepResponse viaCep = viaCepClient.getCep(cep);

            if (viaCep == null || StringUtils.isBlank(viaCep.logradouro())) {
                log.warn("address.lookup.no-result cep={}", cep);
                return new EnderecoResponse(null, null, null);
            }

            log.info("address.lookup.succeeded cep={} cidade={} bairro={}",
                    cep,
                    viaCep.localidade(),
                    viaCep.bairro()
            );

            return new EnderecoResponse(viaCep);
        }
        catch (HttpClientErrorException e) {
            log.warn("address.lookup.invalid-cep cep={} status={}", cep, e.getStatusCode().value());
            throw new AddressNotValidException();
        }
        catch (HttpServerErrorException e) {
            log.warn("address.lookup.external-server-down cep={} status={}", cep, e.getStatusCode().value());
            throw new ExternalServerDownException(ErrorFields.CEP, "Servidor da ViaCep caiu.");
        }
        catch (ResourceAccessException e) {
            log.warn("address.lookup.network-error cep={}", cep);
            throw new ExternalNetworkException(ErrorFields.CEP, "Problema de rede ao acessar o serviço ViaCep");
        }
        catch (RestClientException e) {
            log.warn("address.lookup.rest-client-error cep={}", cep);
            throw new RestTemplateException(ErrorFields.CEP, "Erro no cliente HTTP externo");
        }
    }
}