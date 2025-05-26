
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.realms.assistanceAgent.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {

			boolean isEmployeeCodeUnique = true;
			List<AssistanceAgent> assistanceAgents = this.repository.findAllAgents();

			assistanceAgents.removeIf(agent -> agent.getId() == assistanceAgent.getId());
			String employeeCode = assistanceAgent.getEmployeeCode();
			for (AssistanceAgent agent : assistanceAgents)
				if (agent.getEmployeeCode().equals(employeeCode))
					isEmployeeCodeUnique = false;

			super.state(context, isEmployeeCodeUnique, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
