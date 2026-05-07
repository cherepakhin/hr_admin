<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Создать должность</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500&display=swap" rel="stylesheet">
    <style> body { font-family: 'Inter', sans-serif; } </style>
    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">
</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Create Position page -->
    <!-- На мобилах будет криво выглядеть. -->
    <!-- !!!!!!!!!!! СПЕЦИАЛЬНО ОСТАВИЛ ТАК ДЛЯ ПРИМЕРА !!!!!!!!!!! -->
    <!-- Для приведения в порядок нужно сделать адаптивную верстку, как в create_employee.html (с toolbar слева) -->
    <!-- или в edit_employee.html (там другой вариант, без toolbar слева) -->
    <!-- Sidebar -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <div class="max-w-md mx-auto bg-white shadow-xl rounded-lg overflow-hidden w-full">
            <div class="bg-sky-900 text-white p-6">
                <h1 class="text-xl font-semibold">Новая должность</h1>
            </div>
            <form action="${springMacroRequestContext.contextPath}/positions/" method="post" class="p-6 space-y-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Название</label>
                    <input
                           id="position_name"
                           type="text"
                           name="name"
                           value="${position.name}"
                           required
                           autofocus
                           class="mt-1 block w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-sky-500 focus:border-sky-500">
                    <#if error??>
                        <p class="text-red-600 text-xs mt-1">${error}</p>
                    </#if>
                </div>
                <div class="flex gap-3 pt-2">
                    <button type="submit"
                            class="flex-1 bg-sky-900 text-white py-2 px-4 rounded font-medium hover:bg-sky-800">
                        Сохранить
                    </button>
                    <a href="${springMacroRequestContext.contextPath}/positions/"
                       class="flex-1 bg-gray-200 text-gray-700 py-2 px-4 rounded font-medium text-center hover:bg-gray-300">
                        Отмена
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>