<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Tailwind CSS via CDN -->
    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">

</head>

<body class="bg-gray-50 min-h-screen flex">
    <!-- Show employees page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <div class="w-full mx-auto bg-white shadow-xl border border-gray-100">
            <div class="bg-sky-900 px-6 py-1 text-white text-center">
                <h2 class="text-2xl font-semibold">Список сотрудников</h2>
                <p class="text-sky-200">Страница ${currentPage + 1} из ${totalPages}, всего: ${totalElements} записей</p>
            </div>
            <div class="p-6 overflow-x-auto">
                <#if employees?has_content>
                    <table class="w-full text-sm text-left text-gray-700 table-auto">
                        <thead class="uppercase border-t border-gray-100 bg-gray-50">
                            <tr>
                                <!-- First Name -->
                                <th class="px-6 py-3 cursor-pointer group font-medium font-black"
                                    onclick="window.location.href='?page=${currentPage}&sortField=firstName&direction=${(sortField == 'firstName' && sortDirection == 'asc')?string('desc','asc')}'">
                                    Имя
                                    <span class="text-gray-800 text-2xl">
                                        <#if sortField == "firstName">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                    </span>
                                </th>

                                <!-- Last Name -->
                                <th class="px-6 py-3 cursor-pointer group font-medium font-black"
                                    onclick="window.location.href='?page=${currentPage}&sortField=lastName&direction=${(sortField == 'lastName' && sortDirection == 'asc')?string('desc','asc')}'">
                                    Фамилия
                                    <span class="text-gray-800 text-2xl">
                                        <#if sortField == "lastName">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                    </span>
                                </th>

                                <!-- Email -->
                                <th class="px-6 py-3 cursor-pointer group font-medium font-black"
                                    onclick="window.location.href='?page=${currentPage}&sortField=email&direction=${(sortField == 'email' && sortDirection == 'asc')?string('desc','asc')}'">
                                    Email
                                    <span class="text-gray-800 text-2xl">
                                        <#if sortField == "email">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                    </span>
                                </th>

                                <!-- Position -->
                                <th class="px-6 py-3 cursor-pointer group font-medium font-black"
                                    onclick="window.location.href='?page=${currentPage}&sortField=position.name&direction=${(sortField == 'position.name' && sortDirection == 'asc')?string('desc','asc')}'">
                                    Должность
                                    <span class="text-gray-800 text-2xl">
                                        <#if sortField == "position.name">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                    </span>
                                </th>

                                <!-- Actions -->
                                <th class="px-6 py-3 cursor-pointer group font-medium font-black text-center">Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Employee Details -->
                            <#list employees as emp>
                            <tr class="border-b border-gray-100 hover:bg-gray-50 transition">
                                <td class="px-6 py-4 font-medium">${emp.firstName}</td>
                                <td class="px-6 py-4">${emp.lastName}</td>
                                <td class="px-6 py-4">${emp.email}</td>
                                <td class="px-6 py-4">${emp.position.name}</td>
                                <td class="px-6 py-4 flex justify-center gap-4">
                                    <a href="${springMacroRequestContext.contextPath}/employees/edit/${emp.id}"
                                       class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                       title="Изменить">
                                        <svg class="action-icon" xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="-4 -4 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                        </svg>
                                    </a>

                                    <!-- Delete Action -->
                                    <a href="${springMacroRequestContext.contextPath}/employees/delete/${emp.id}"
                                       onclick="return confirm('Вы уверены, что хотите удалить сотрудника ${emp.firstName} ${emp.lastName}?');"
                                       class="action-delete inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-blue-100 transition"
                                       title="Удалить">
                                        <svg class="action-icon" xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="-4 -4 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <div class="flex justify-between items-center mt-6 font-medium">
                        <div class="text-sm text-gray-600">
                            Показано ${(currentPage * 10) + 1}–${(currentPage * 10) + employees?size} из ${totalElements}
                        </div>

                        <ul class="inline-flex space-x-2 text-lg">
                            <!-- First Page Button -->
                            <#if currentPage gt 0>
                                <li>
                                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=0${'&sortField=' + sortField + '&direction=' + sortDirection!'&sortField=id&direction=asc'}" class="pagination-first-last text-2xl">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                            width="36" height="36" viewBox="0 3 16 24">
                                                <title>В начало</title>
                                                <path fill="currentColor" d="m16.293 17.707l1.414-1.414L13.414 12l4.293-4.293l-1.414-1.414L10.586 12zM7 6h2v12H7z"/>
                                        </svg>
                                   </a>
                                </li>
                            <#else>
                                <li>
                                    <span class="pagination-first-last opacity-50 cursor-not-allowed text-2xl">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                            width="36" height="36" viewBox="0 3 16 24">
                                                <title>В начало</title>
                                                <path fill="currentColor" d="m16.293 17.707l1.414-1.414L13.414 12l4.293-4.293l-1.414-1.414L10.586 12zM7 6h2v12H7z"/>
                                        </svg>
                                    </span>
                                </li>
                            </#if>

                            <!-- Previous Button -->
                            <#if currentPage gt 0>
                                <li>
                                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${currentPage - 1}${'&sortField=' + sortField + '&direction=' + sortDirection!'&sortField=id&direction=asc'}" class="pagination-link  text-2xl">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                            width="36" height="36" viewBox="10 3 16 24">
                                            <title>Chevron-left SVG Icon</title><path fill="currentColor" d="M13.293 6.293L7.586 12l5.707 5.707l1.414-1.414L10.414 12l4.293-4.293z"/></svg>
                                    </a>
                                </li>
                            <#else>
                                <li>
                                    <span class="pagination-link opacity-50 cursor-not-allowed text-2xl">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                            width="36" height="36" viewBox="0 3 16 24">
                                        <title>Chevron-left SVG Icon</title><path fill="currentColor" d="M13.293 6.293L7.586 12l5.707 5.707l1.414-1.414L10.414 12l4.293-4.293z"/></svg>
                                    </span>
                                </li>
                            </#if>

                            <!-- Page Numbers with Current Highlighted -->
                            <#assign startPage = [currentPage - 2, 0]?max />
                            <#assign endPage = [(startPage + 4), totalPages - 1]?min />

                            <#list startPage..endPage as p>
                                <li>
                                    <#if p == currentPage>
                                        <span class="pagination-current">${p + 1}</span>
                                    <#else>
                                        <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${p}${'&sortField=' + sortField + '&direction=' + sortDirection!'&sortField=id&direction=asc'}" class="pagination-link">${p + 1}</a>
                                    </#if>
                                </li>
                            </#list>

                            <!-- Next Button -->
                            <#if currentPage lt totalPages - 1>
                                <li>
                                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${currentPage + 1}${'&sortField=' + sortField + '&direction=' + sortDirection!'&sortField=id&direction=asc'}" class="pagination-link">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                        width="36" height="36" viewBox="0 3 16 24">
                                        <title>Следующая страница</title><path fill="currentColor" d="M10.707 17.707L16.414 12l-5.707-5.707l-1.414 1.414L13.586 12l-4.293 4.293z"/></svg>
                                    </a>
                                </li>
                            <#else>
                                <li>
                                    <span class="pagination-link opacity-50 cursor-not-allowed">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                        width="36" height="36" viewBox="0 3 16 24">
                                        <title>Следующая страница</title><path fill="currentColor" d="M10.707 17.707L16.414 12l-5.707-5.707l-1.414 1.414L13.586 12l-4.293 4.293z"/></svg>
                                    </span>
                                </li>
                            </#if>

                            <!-- Last Page Button -->
                            <#if currentPage lt totalPages - 1>
                                <li>
                                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${totalPages - 1}${'&sortField=' + sortField + '&direction=' + sortDirection!'&sortField=id&direction=asc'}" class="pagination-first-last">
                                        <svg xmlns="https://www.w3.org/2000/svg"
                                        width="36" height="36" viewBox="10 3 16 24">
                                        <title>Last-page SVG Icon</title><path fill="currentColor" d="M7.707 17.707L13.414 12L7.707 6.293L6.293 7.707L10.586 12l-4.293 4.293zM15 6h2v12h-2z"/></svg>
                                    </a>
                                </li>
                            <#else>
                                <li class="pagination-link opacity-50 cursor-not-allowed">
                                    <svg xmlns="https://www.w3.org/2000/svg"
                                    width="36" height="36" viewBox="0 3 16 24">
                                    <title>Last-page SVG Icon</title><path fill="currentColor" d="M7.707 17.707L13.414 12L7.707 6.293L6.293 7.707L10.586 12l-4.293 4.293zM15 6h2v12h-2z"/></svg>
                                </li>
                            </#if>
                        </ul>
                    </div>
                <#else>
                    <p class="text-center py-6 text-gray-500">Сотрудники не найдены.</p>
                </#if>
            </div>
        </div>
    </div>
</body>
</html>