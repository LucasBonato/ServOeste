package com.serv.oeste.Tecnico.Command.CommandHandler.Models;

import com.serv.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateTecnicoCommand {
    private Integer id;
    private TecnicoDTO tecnicoDTO;
}
