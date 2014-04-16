package org.zero.hadoop.manager.service.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.node.dao.NodeDao;

@Controller
public class NodeListCtr {

	@Autowired
	NodeDao nodeDao; 
	
	@RequestMapping("/node")
	public Model getNodeList(Model model) {
		
		System.out.println("Node Request Method Call");
		return model.addAttribute("_rslt", nodeDao.getNodeList().values());
	}
}
