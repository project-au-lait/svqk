import { locale } from '$lib/translations';
import { reporter } from '@felte/reporter-svelte';
import { validator } from '@felte/validator-yup';
import { createForm } from 'felte';
import * as yup from 'yup';
import * as ja from 'yup-locale-ja';

export default class FormValidator {
  static createForm(
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    spec: any,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    ...submitHandlers: ((values?: any) => void)[]
  ) {
    if (locale.get() === 'ja') {
      yup.setLocale(ja.suggestive);
    }

    const schema = yup.object(spec);

    const { form } = createForm({
      onSubmit: (values, context) => {
        if (submitHandlers.length == 0) {
          console.error('No submit handler defined');
        } else if (submitHandlers.length == 1) {
          submitHandlers[0](values);
        } else if (
          context.event &&
          context.event instanceof SubmitEvent &&
          context.event.submitter
        ) {
          const handlerName = context.event.submitter.dataset.handler;

          if (!handlerName) {
            console.error(
              `No data-handler attribute found for submitter ${context.event.submitter}` //NOSONAR
            );
            return;
          }
          const submitHandler = submitHandlers.find((handler) => handler.name == handlerName);
          if (!submitHandler) {
            console.error(`No submit handler found for submitter ${context.event.submitter}`); //NOSONAR
            return;
          }
          submitHandler(values);
        }
      },

      extend: [validator({ schema }), reporter]
    });

    return form;
  }
}
