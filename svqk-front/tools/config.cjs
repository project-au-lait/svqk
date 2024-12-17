exports.hooks = {
  onFormatRouteName: (routeInfo, templateRouteName) => {
    let newTemplateRouteName = templateRouteName.replace(/SearchCreate$/, 'Search');

    console.log(`     Replace templateRouteName ${templateRouteName} to ${newTemplateRouteName}`);
    return newTemplateRouteName;
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
