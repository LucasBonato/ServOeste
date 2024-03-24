package com.sev.oeste.Tecnico.Command.CommandHandler.Models;

import com.sev.oeste.Tecnico.Models.DTOs.TecnicoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateTecnicoCommand {
    private Integer id;
    private TecnicoDTO tecnicoDTO;
}
