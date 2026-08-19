<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Edit Employee - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>body{font-family:'Inter',sans-serif;}.btn-dark-blue{@apply bg-sky-900 hover:bg-sky-800 text-white;}</style>
    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-2">

    <!-- Edit employee page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <div class="w-full max-w-lg bg-white shadow-xl border border-gray-100">
        <div class="bg-sky-900 px-3 py-3 text-white text-center">
            <h2 class="text-2xl font-semibold">Изменить информацию о сотруднике</h2>
            <p class="text-sky-200">Update employee information</p>
        </div>
        <form action="${springMacroRequestContext.contextPath}/employees/update/${employee.id}" method="post" class="p-6 space-y-3 pt-0">
            <input type="hidden" name="id" value="${employee.id}" />
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
                <input name="firstName" value="${employee.firstName}" class="w-full px-4 py-3 border border-gray-300 focus:ring-0 focus:ring-sky-500" required />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Фамилия</label>
                <input name="lastName" value="${employee.lastName}" class="w-full px-4 py-3 border border-gray-300 focus:ring-0 focus:ring-sky-500" required />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input name="email" value="${employee.email}" type="email" class="w-full px-4 py-3 border border-gray-300 focus:ring-0 focus:ring-sky-500" required />
            </div>
            <!-- Position -->
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700" for="position_select">Должность</label>
                <select
                    id = "position_select"
                    name="position.id"
                    class="mt-1 block w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-sky-500 focus:border-sky-500" required>
                    <#list positions as position>
                        <option value="${position.id}" <#if position.id == employee.position.id>selected="selected"</#if>>
                          ${position.name}
                        </option>
                    </#list>
                </select>
            </div>
            <div class="flex gap-3 pt-2">
                <button type="submit" class="btn-dark-blue w-full py-3 px-4 font-medium  hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">Обновить</button>
                <#if prevPage == '/show_employees'>
                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees"
                        class="btn-dark-blue w-full py-3 px-4 font-medium text-center' hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900 text-center">Назад</a>
                <#else>
                    <a href="${springMacroRequestContext.contextPath}${prevPage}"
                        class="btn-dark-blue w-full py-3 px-4 font-medium hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900 text-center">Назад</a>
                </#if>
            </div>
        </form>
    </div>
<!--  Для отладки истории <div>contextPath:${springMacroRequestContext.contextPath} PrevPage:${prevPage}</div> -->

    <!-- Include Bottom Navigation Fragment (mobile only) -->
    <#include "fragments/bottom-navigation.ftl">
    <!-- End Include Bottom Navigation Fragment -->
</body>
</html>
