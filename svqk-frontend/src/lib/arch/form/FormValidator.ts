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
    onSubmit: (values?: any) => void
  ) {
    if (locale.get() === 'ja') {
      yup.setLocale(ja.suggestive);
    }

    const schema = yup.object(spec);

    const { form } = createForm({
      onSubmit,
      extend: [validator({ schema }), reporter]
    });

    return form;
  }
}
