package es.elovendo.service.admin.report.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.elovendo.model.admin.report.item.ItemReport;
import es.elovendo.model.admin.report.item.ItemReportRepository;
import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.service.item.ItemService;

@Service("itemReportService")
public class ItemReportServiceImpl implements ItemReportService {
	
	@Autowired
	private ItemReportRepository itemReportRepository;
	@Autowired
	private ItemService itemService;

	@Override
	public ItemReport createItemReport(Long itemId, User user, String reportMessage) throws ItemNotFoundException {
		Item item = itemService.getItemById(itemId);
		ItemReport report = new ItemReport(item, user, reportMessage);
		return itemReportRepository.save(report);
	}

	@Override
	public void deleteItemReport(Long itemReportId) {
		itemReportRepository.delete(itemReportId);
	}

	@Override
	public Page<ItemReport> getAllItemReport(int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return itemReportRepository.findAll(request);
	}

}
