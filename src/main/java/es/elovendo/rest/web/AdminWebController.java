package es.elovendo.rest.web;

import static es.elovendo.util.Constant.S_ITEMS_PER_PAGE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.elovendo.model.admin.report.item.ItemReport;
import es.elovendo.model.admin.report.user.UserReport;
import es.elovendo.model.item.Item;
import es.elovendo.service.admin.report.item.ItemReportService;
import es.elovendo.service.admin.report.user.UserReportService;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.PageWrapper;

@Controller
@RequestMapping(value = "/private/admin/")
public class AdminWebController {
	
	@Autowired
	private ItemReportService itemReportService;
	@Autowired
	private UserReportService userReportService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/home")
	public String loginPage() {
		return "/elovendo/admin/adminHome";
	}
	
	/* Item report */
	@RequestMapping(value = "/item-report", method = RequestMethod.GET)
	public String itemReportsPage(Model model) {
		
		String url = "item-report";
		Page<ItemReport> reports = itemReportService.getAllItemReport(0, 10);
		PageWrapper<ItemReport> itemReports = new PageWrapper<ItemReport>(reports, url);
		
		model.addAttribute("reports", itemReports.getContent());
		
		return "/elovendo/admin/itemReports";
	}
	
	/* User report */
	@RequestMapping(value = "/user-report", method = RequestMethod.GET)
	public String userReportsPage(Model model) {
		
		String url = "user-report";
		Page<UserReport> reports = userReportService.getAllUserReport(0, 10);
		PageWrapper<UserReport> userReports = new PageWrapper<UserReport>(reports, url);
		
		model.addAttribute("reports", userReports.getContent());
		
		return "/elovendo/admin/userReports";
	}
	
	@RequestMapping(value = "/votes-watcher", method = RequestMethod.GET)
	public String votesWatcherPage() {
		return "/elovendo/admin/votesWatcher";
	}
	
	/* Last items */
	@RequestMapping(value = "/last-items", method = RequestMethod.GET)
	public String lastItemsPage(Model model,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {
		
		String url = "last-items";
		Page<Item> items = itemService.getAllItems(page, size);
		PageWrapper<Item> itemsWrapper = new PageWrapper<Item>(items, url);

		model.addAttribute("page", itemsWrapper);
		model.addAttribute("items", itemsWrapper.getContent());
		
		return "/elovendo/admin/lastItems";
	}
	
	/* Last users */
	@RequestMapping(value = "/last-users", method = RequestMethod.GET)
	public String lastUsersPage() {
		return "/elovendo/admin/lastUsers";
	}
	
	/******************************************************************************/
	/******************************************************************************/
	/**********************            ADMIN ACTIONS            *******************/
	/******************************************************************************/
	/******************************************************************************/
	
	/* Delete item report */
	@RequestMapping(value = "/delete/item/report/{itemReportId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteItemReportPost(@PathVariable("itemReportId") long itemReportId) {
		itemReportService.deleteItemReport(itemReportId);
	}
	
	/* Delete item */
	@RequestMapping(value = "/delete/item/{itemId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteItemPost(@PathVariable("itemId") long itemId) {
		itemService.deleteItem(itemId);
	}
	
	/* Delete user report */
	@RequestMapping(value = "/delete/user/report/{userReportId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteUserReportPost(@PathVariable("userReportId") long userReportId) {
		userReportService.deleteUserReport(userReportId);
	}
	
	/* Delete user */
	@RequestMapping(value = "/delete/user/{userId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteUserPost(@PathVariable("userId") long userId) {
		userService.deleteUser(userId);
	}
}
