package br.com.gestaoprodutos.api_produtos;

import br.com.gestaoprodutos.api_produtos.domain.model.Produto;
import org.springframework.batch.item.ItemProcessor;

public class ProdutoProcessor implements ItemProcessor<Produto, Produto> {

    @Override
    public Produto process(Produto item) throws Exception {
        return item;
    }
}
