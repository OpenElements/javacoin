package eu.javaland.javacoin.backend.coin;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CoinFreemarkerEndpoint {

    private final CoinContractService contractService;

    public CoinFreemarkerEndpoint(final CoinContractService contractService) {
        this.contractService = Objects.requireNonNull(contractService, "contractService must not be null");
    }


    @GetMapping("/admin")
    public String getAdmin(final Model model) {
        Objects.requireNonNull(model);
        model.addAttribute("coinAmount", contractService.getCoinAmount());
        model.addAttribute("coinsInPool", contractService.getCoinsInPool());
        model.addAttribute("coins", contractService.getCoinsForAccount());
        model.addAttribute("hbarInPool", contractService.getHbarInPool());
        model.addAttribute("hbar", contractService.getHBarForAccount());
        model.addAttribute("coinPrice", contractService.getCoinPrice());
        return "admin";
    }

    @PostMapping("/onbuy")
    public String onBuy(final String amount, final Model model) {

        contractService.buyCoins(new BigDecimal(amount));
        return getIndex(model);
    }

    @PostMapping("/onsell")
    public String onSell(final String amount, final Model model) {

        contractService.sellCoins(new BigDecimal(amount));
        return getIndex(model);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(final Model model) {
        Objects.requireNonNull(model);
        model.addAttribute("coinAmount", contractService.getCoinAmount());
        model.addAttribute("coinsInPool", contractService.getCoinsInPool());
        model.addAttribute("coins", contractService.getCoinsForAccount());
        model.addAttribute("hbar", contractService.getHBarForAccount());
        model.addAttribute("coinPrice", contractService.getCoinPrice());
        return "index";
    }

    @PostMapping("/onmint")
    public String onMint(final String amount, final Model model) {

        contractService.mintCoins(new BigDecimal(amount));
        return getAdmin(model);
    }

    @PostMapping("/onburn")
    public String onBurn(final String amount, final Model model) {

        contractService.burnCoins(new BigDecimal(amount));
        return getAdmin(model);
    }

    @PostMapping("/ondeposit")
    public String onDeposit(final String amount, final Model model) {

        contractService.depositHbars(new BigDecimal(amount));
        return getAdmin(model);
    }

    @PostMapping("/onwithdraw")
    public String onWithdraw(final String amount, final Model model) {

        contractService.withdrawHbars(new BigDecimal(amount));
        return getAdmin(model);
    }


}
