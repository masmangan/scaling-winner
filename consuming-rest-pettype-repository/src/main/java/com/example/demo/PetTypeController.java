// Copyright (c) 2020, the REST Pet Type Repository project authors.  Please see
// the AUTHORS file for details. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.	

package com.example.demo;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 
 * @author marco.mangan@pucrs.br
 *
 */
@Controller
public class PetTypeController {

	/**
	 * 
	 */
	private static final String PET_TYPE_LIST = "petTypeList";

	/**
	 * 
	 */
	private static final String PET_TYPE_DETAILS = "petTypeDetails";

	/**
	 * 
	 */
	private static final String CREATE_OR_UPDATE_PET_TYPE_FORM = "createOrUpdatePetTypeForm";

	/**
	 * 
	 */
	@Autowired
	private PetTypeRepository petTypes;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/pettypes")
	public String showPetTypeList(Model model) {
		model.addAttribute("types", petTypes.findAll());
		return PET_TYPE_LIST;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/pettypes/new")
	public String formPetTypeList(Model model) {
		model.addAttribute("type", new PetType());
		return CREATE_OR_UPDATE_PET_TYPE_FORM;
	}

	/**
	 * 
	 * @param petType
	 * @param result
	 * @return
	 */
	@PostMapping("/pettypes/new")
	public String addPetTypeList(@Valid PetType petType, BindingResult result) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_PET_TYPE_FORM;
		} else {
			petTypes.save(petType);
			return "redirect:/pettypes/" + petType.getId();
		}
	}

	/**
	 * 
	 * @param petTypeId
	 * @param model
	 * @return
	 */
	@GetMapping("/pettypes/{petTypeId}/edit")
	public String initUpdateOwnerForm(@PathVariable("petTypeId") int petTypeId, Model model) {
		PetType type = this.petTypes.findById(petTypeId);
		model.addAttribute("type", type);
		return CREATE_OR_UPDATE_PET_TYPE_FORM;
	}

	/**
	 * 
	 * @param petType
	 * @param result
	 * @param petTypeId
	 * @return
	 */
	@PostMapping("/pettypes/{petTypeId}/edit")
	public String processUpdateOwnerForm(@Valid PetType petType, BindingResult result,
			@PathVariable("petTypeId") int petTypeId) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_PET_TYPE_FORM;
		} else {
			petType.setId(petTypeId);
			this.petTypes.save(petType);
			return "redirect:/pettypes/{petTypeId}";
		}
	}

	/**
	 * 
	 * @param petTypeId
	 * @param model
	 * @return
	 */
	@GetMapping("/pettypes/{petTypeId}")
	public String showOwner(@PathVariable("petTypeId") int petTypeId, Model model) {
		PetType type = this.petTypes.findById(petTypeId);
		model.addAttribute("type", type);
		return PET_TYPE_DETAILS;
	}

}
