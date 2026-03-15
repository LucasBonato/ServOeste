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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
@RequiredArgsConstructor
public class AddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    private final ViaCepClient viaCepClient;

    public EnderecoResponse getFields(String cep) {
        LOGGER.debug("address.lookup.started cep={}", cep);
        try {
            ViaCepResponse viaCep = viaCepClient.getCep(cep);

            if (viaCep == null || StringUtils.isBlank(viaCep.logradouro())) {
                LOGGER.warn("address.lookup.no-result cep={}", cep);
                return new EnderecoResponse(null, null, null);
            }

            LOGGER.info("address.lookup.succeeded cep={} cidade={} bairro={}",
                    cep,
                    viaCep.localidade(),
                    viaCep.bairro()
            );

            return new EnderecoResponse(viaCep);
        }
        catch (HttpClientErrorException e) {
            LOGGER.warn("address.lookup.invalid-cep cep={} status={}", cep, e.getStatusCode().value());
            throw new AddressNotValidException();
        }
        catch (HttpServerErrorException e) {
            LOGGER.warn("address.lookup.external-server-down cep={} status={}", cep, e.getStatusCode().value());
            throw new ExternalServerDownException(ErrorFields.CEP, "Servidor da ViaCep caiu.");
        }
        catch (ResourceAccessException e) {
            LOGGER.warn("address.lookup.network-error cep={}", cep);
            throw new ExternalNetworkException(ErrorFields.CEP, "Problema de rede ao acessar o serviço ViaCep");
        }
        catch (RestClientException e) {
            LOGGER.warn("address.lookup.rest-client-error cep={}", cep);
            throw new RestTemplateException(ErrorFields.CEP, "Erro no cliente HTTP externo");
        }
    }
}