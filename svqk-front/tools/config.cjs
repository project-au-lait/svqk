exports.hooks = {
  onFormatRouteName: (routeInfo, templateRouteName) => {
    const nextCharOfVersion = templateRouteName.match(/^v\d(.)/);

    if (nextCharOfVersion) {
      let newTemplateRouteName = templateRouteName.replace(
        /^v\d./,
        nextCharOfVersion[1].toLowerCase()
      );

      newTemplateRouteName = newTemplateRouteName.replace(/SearchCreate$/, 'Search');

      console.log(`     Replace templateRouteName ${templateRouteName} to ${newTemplateRouteName}`);
      return newTemplateRouteName;
    }

    return templateRouteName;
  },
  onFormatTypeName: (typeName, rawTypeName, schemaType) => {
    if (schemaType === 'type-name' && typeName.endsWith('Dto')) {
      const newTypeName = typeName.replace(/Dto$/, 'Model');
      console.log(`    Replace typeName ${typeName} to ${newTypeName}`);
      return newTypeName;
    }

    return typeName;
  }
};
