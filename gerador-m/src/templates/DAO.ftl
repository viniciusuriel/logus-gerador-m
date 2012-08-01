package ${packageName};

<#list imports as import>import ${import};
</#list>
import ${voPackageName}.${entityName}VO;
import ${daoInterfacePackageName}.${entityName}DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logus.userContext.ContextualizedDAO;
import logus.userContext.UserContext;
import logus.util.integracao.Transaction;
import logus.util.model.ValidationException;
import logus.util.model.ValueObject;

/**
 * DAO de ${description} {@link ${entityName}VO}
 * 
 * @author ${user}
 * 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ${entityName}DAO extends ContextualizedDAO<${entityName}VO> implements ${entityName}DAOInterface {

	/**
	 * Uma instância para cada contexto.
	 */	
	private static Map instances = new HashMap();

	/**
	 * Privado para garantir que somente um DAO seja produzido para cada
	 * contexto.
	 * 
	 * @param userContext
	 */
	private ${entityName}DAO(UserContext userContext) {
		super(userContext);
	}

	/**
	 * Retorna a instância do DAO vinculada a um contexto específico.
	 * 
	 * @param userContext
	 *            o contexto do usuário que estabelece o DAO a ser retornado.
	 * @return o DAO vinculado ao contexto do usuário.
	 */	
	public static ${entityName}DAO getInstance(UserContext userContext) {
		${entityName}DAO instance = (${entityName}DAO) instances
				.get(userContext.toString());
		if (instance == null) {
			instance = new ${entityName}DAO(userContext);
			instances.put(userContext.toString(), instance);
		}
		return instance;
	}

	/**
	 * Carrega todas as ${description}.
	 * 
	 * @return lista contendo todas as ${description}.
	 * @throws SQLException
	 *             caso algum erro ocorra na comunicação com o banco.
	 */
	@Override
	protected List<${entityName}VO> dbLoadAll() throws SQLException {
		StringBuilder sb = new StringBuilder("select ")
				.append(ANO_EXERCICIO_CTX).append(", ")
				.append(COD_CLIENTE_CTX).append(", ")
				<#list fields as field>								
				.append(COL_${field.columnName})<#if field_has_next>.append(", ")</#if>
				</#list>
				.append(" from ").append(tablePrefix())
				.append(TABLE_NAME).append(" ent ");
		return loadListByStatement(appendContextFilter(sb.toString(), "ent"),false);
	}

	/**
	 * Carrega um Detalhamento de fonte de acordo com sua chave.
	 * 
	 * @param key
	 *            array de objetos que formam a chave do ${description}
	 * @return O objeto representando o ${description} ou null caso não
	 *         exista.
	 * @throws SQLException
	 *             caso algum problema não previsto ocorra na comunicação com o
	 *             banco de dados.
	 */
	@Override
	protected ${entityName}VO dbLoad(Object[] key) throws SQLException {
		StringBuilder sb = new StringBuilder("select ")
				.append(ANO_EXERCICIO_CTX).append(", ")
				.append(COD_CLIENTE_CTX).append(", ")
		<#list fields as field>									
				.append(COL_${field.columnName}).append(", ")
		</#list>				
				.append(" from ").append(tablePrefix())
				.append(TABLE_NAME).append(" ent ")
				.append(" where ");
				//TODO completar o select
		return loadByStatement(appendContextFilter(sb.toString(), "ent"), false);
	}

	/**
	 * Insere um novo ${description} no banco de dados.
	 * 
	 * @param t
	 *            transação onde a inserção está inserida.
	 * @param vo
	 *            o novo ${description} a ser inserida no banco.
	 * @throws SQLException
	 *             caso algum problema não previsto ocorra na comunicação com o
	 *             banco de dados.
	 */
	@Override
	protected void dbInsert(Transaction t, ${entityName}VO vo)
			throws SQLException {
		t.begin();
		try {
			StringBuilder sb = new StringBuilder("insert into ")
					.append(tablePrefix()).append(TABLE_NAME)
					.append(" ( ")
					<#list fields as field>								
					.append(COL_${field.columnName})<#if field_has_next>.append(", ")</#if>
					</#list>
					.append(" ) values (<#list fields as field>?<#if field_has_next>,</#if></#list>)");

			PreparedStatement ps = t.getConnection().prepareStatement(appendInsertContext(sb.toString()));
			
			<#compress>
				<#list fields as field>												
				<#if field.typeFullName == "java.lang.String">ps.setString(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Character">ps.setString(${field_index + 1}, Character.toString(vo.get${field.name?capitalize}()));</#if>
				<#if field.typeFullName == "java.lang.Integer">ps.setInt(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Float">ps.setFloat(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Long">ps.setLong(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Byte">ps.setByte(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Short">ps.setShort(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Boolean">ps.setBoolean(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Double">ps.setDouble(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.BigDecimal">ps.setBigDecimal(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Date">ps.setDate(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Date">ps.setDate(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Time">ps.setTime(${field_index + 1}, vo.get${field.name?capitalize}());</#if>
				<#if field.typeFullName == "java.lang.Timestamp">ps.setTimestamp(${field_index + 1}, vo.get${field.name?capitalize}());</#if>			
				</#list>
			</#compress>
			
			bindContext(ps, ${fields?size + 1}});			
			
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			t.rollBack();
			throw e;
		}
		t.commit();
	}

	/**
	 * Atualiza um ${description} no banco de dados.
	 * 
	 * @param t
	 *            transação onde a atualização está inserida.
	 * @param vo
	 *            o ${description} a ser atualizada no banco.
	 * @throws SQLException
	 *             caso algum problema não previsto ocorra na comunicação com o
	 *             banco de dados.
	 */
	@Override
	protected void dbUpdate(Transaction t, ${entityName}VO vo)
			throws SQLException {
		PreparedStatement ps = null;
		t.begin();
		try {
			StringBuilder sb = new StringBuilder("update ")
					.append(tablePrefix()).append(TABLE_NAME)
					.append(" ent set ")															
					.append(" where ");
					//TODO completar update
			ps = t.getConnection().prepareStatement(appendContextFilter(sb.toString(), "ent"));						
			
			ps.executeUpdate();			
		} catch (SQLException e) {
			t.rollBack();
			throw e;
		} finally{
			closeStatement(ps);
		}
		t.commit();
	}

	/**
	 * Exclui um ${description} do banco de dados.
	 * 
	 * @param t
	 *            transação
	 * @param key
	 *            chave do objeto
	 */
	@Override
	protected void dbDelete(Transaction t, Object[] key) throws SQLException {
		t.begin();
		try {
			StringBuilder sb = new StringBuilder(" delete from ")
					.append(tablePrefix()).append(TABLE_NAME)
					.append(" ent where ");
			//TODO completar a delecao
			String sql = appendContextFilter(sb.toString(), "ent");
			handleDelete(t.getConnection(), sql);
			PreparedStatement ps = t.getConnection().prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			t.rollBack();
			throw e;
		}
		t.commit();
	}

	/**
	 * Retorna a chave do ${description}.
	 * 
	 * @param obj
	 *            objeto cuja chave será retornada.
	 * @return array contendo a chave do ${description}.
	 */
	protected Object[] keyOf(${entityName}VO obj) {
		return obj.keyOf();
	}

	/**
	 * Carrega um ${description} a partir do registro corrente de um
	 * resultset.
	 * 
	 * @param rs
	 *            resultset de onde o objeto será carregado.
	 * @return o objeto carregado.
	 * @throws SQLException
	 *             se algum problema ocorrer no acesso ao banco de dados.
	 */
	@Override
	protected ${entityName}VO rsToObject(ResultSet rs) throws SQLException {
		${entityName}VO obj = new ${entityName}VO();
		<#compress> 				
		<#list fields as field>						
			<#if field.typeFullName == "java.lang.String">obj.set${field.name?capitalize}(rs.getString(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Character">obj.set${field.name?capitalize}(rs.getString(COL_${field.columnName}).charAt(0));</#if>
			<#if field.typeFullName == "java.lang.Integer">obj.set${field.name?capitalize}(rs.getInt(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Float">obj.set${field.name?capitalize}(rs.getFloat(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Long">obj.set${field.name?capitalize}(rs.getLong(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Byte">obj.set${field.name?capitalize}(rs.getByte(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Short">obj.set${field.name?capitalize}(rs.getShort(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Boolean">obj.set${field.name?capitalize}(rs.getBoolean(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.lang.Double">obj.set${field.name?capitalize}(rs.getDouble(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.math.BigDecimal">obj.set${field.name?capitalize}(rs.getBigDecimal(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.util.Date">obj.set${field.name?capitalize}(rs.getDate(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.sql.Date">obj.set${field.name?capitalize}(rs.getDate(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.sql.Time">obj.set${field.name?capitalize}(rs.getTime(COL_${field.columnName}));</#if>
			<#if field.typeFullName == "java.sql.Timestamp">obj.set${field.name?capitalize}(rs.getTimestamp(COL_${field.columnName}));</#if>
  		</#list>						
		 </#compress>
		return obj;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param obj
	 *            {@inheritDoc}
	 */
	@Override
	protected ${entityName}VO dbFullLoad(${entityName}VO obj,
			Map<Object, ValueObject> tempCache) throws SQLException {
		//TODO Implementar fullLoad
		return obj;
	}

	// ----------------------------------------------------------------------------
	// Validação
	// ----------------------------------------------------------------------------

	/**
	 * Gera uma exceção empacotando todos os erros encontrados caso um objeto
	 * não seja válido para persistêcia.
	 * 
	 * @param con
	 *            Conexão com o banco de dados.
	 * @param vo
	 *            objeto a ser validado.
	 */
	@Override
	protected void validate(Connection con, ${entityName}VO vo)
			throws SQLException, ValidationException {
		//TODO Implementar validate
	}

}
